import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = 'http://localhost:8080';

export const options = {
    scenarios: {
        duplicate_delete: {
            executor: 'shared-iterations',
            vus: 10,
            iterations: 10,
            maxDuration: '30s',
        },
    },
};

export function setup() {
    const res = http.post(
        `${BASE_URL}/extensions/custom`,
        JSON.stringify({ name: 'deltest' }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    const data = JSON.parse(res.body);
    const target = data.custom.find(e => e.name === 'deltest');

    console.log(`테스트 대상 ID: ${target.id}`);
    return { id: target.id };
}

export default function (data) {
    const res = http.del(`${BASE_URL}/extensions/custom/${data.id}`);

    const ok = res.status === 200;
    const notFound = res.status === 400;

    console.log(`VU ${__VU} | status: ${res.status} | ${ok ? '삭제 성공' : notFound ? '이미 삭제됨' : '서버 오류'}`);

    check(res, {
        '500 에러 없음': (r) => r.status !== 500,
    });
}

export function teardown() {
    const res = http.get(`${BASE_URL}/extensions`);
    const data = JSON.parse(res.body);
    const exists = data.custom.find(e => e.name === 'deltest');

    console.log(`\n=== 결과 ===`);
    console.log(`deltest 잔존 여부: ${exists ? '남아있음 (오류)' : '정상 삭제됨'}`);
}
