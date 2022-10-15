/**
 * 요약: 배열에 항목 한 개 삽입하기
 * 배열.splice(삽입할_위치, 0, 삽입할_값);
 */
let a = [0, 1, 2];
a.splice(1, 0, "a");
console.log(a);


/**
 * 요약: 배열에서 항목 한 개 삭제하기
 * 배열.splice(삭제할_위치, 1);
 */
let b = [0, 1, 2];
b.splice(1, 1);
console.log(b);

