import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:8080';

export const options = {
    scenarios: {
        race_condition: {
            executor: 'shared-iterations',
            vus: 10,
            iterations: 10,
            maxDuration: '30s',
        },
    },
};

export function setup() {
    console.log('=== Setup: 커스텀 확장자 199개 등록 시작 ===');

    const current = http.get(`${BASE_URL}/extensions`);
    const data = JSON.parse(current.body);
    const existingCount = data.custom.length;
    console.log(`현재 커스텀 확장자 수: ${existingCount}`);

    data.custom.forEach(ext => {
        http.del(`${BASE_URL}/extensions/custom/${ext.id}`);
    });

    let registered = 0;
    for (let i = 1; i <= 199; i++) {
        const name = 'a' + String(i).padStart(3, '0');
        const res = http.post(
            `${BASE_URL}/extensions/custom`,
            JSON.stringify({ name }),
            { headers: { 'Content-Type': 'application/json' } }
        );
        if (res.status === 201) registered++;
    }

    console.log(`등록 완료: ${registered}개`);
    console.log('=== 이제 10명이 동시에 200번째 확장자 추가 시도 ===');
    sleep(1);
}

export default function () {
    const num = 199 + __VU;
    const name = 'a' + String(num).padStart(3, '0');

    const res = http.post(
        `${BASE_URL}/extensions/custom`,
        JSON.stringify({ name }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const success = res.status === 201;
    const body = res.status !== 201 ? JSON.parse(res.body).message : '';

    console.log(`VU ${__VU} | name: ${name} | status: ${res.status} | ${success ? '✅ 추가 성공' : '❌ ' + body}`);

    check(res, {
        '201 or 400 응답': (r) => r.status === 201 || r.status === 400,
    });
}

export function teardown() {
    sleep(1);
    console.log('\n=== Teardown: 최종 결과 확인 ===');

    const res = http.get(`${BASE_URL}/extensions`);
    const data = JSON.parse(res.body);
    const finalCount = data.custom.length;

    console.log(`최종 커스텀 확장자 수: ${finalCount}`);

    if (finalCount > 200) {
        console.log(`Race Condition 발생. 200개 제한을 초과했습니다: ${finalCount}개`);
    } else {
        console.log(`200개 이하 유지 (${finalCount}개)`);
    }
}
