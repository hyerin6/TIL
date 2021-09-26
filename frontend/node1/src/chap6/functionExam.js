// 함수 정의 & 호출
function hello(name) {
    console.log("hello " + name);
}
hello("홍길동");

function add(a, b) {
    return a + b;
}
console.log(add(3, 4));


// 파라미터 값 생략 가능
function myName(name) {
    console.log("hello " + name);
}
myName();

// 3 + undefined 표현식의 값은, 계산할 수 없으므로, NaN 이다.
console.log(add(3));


// 파라미터 값이 전달되었는지 확인
function add2(a, b) {
    if (b == undefined) b = 0;
    // if (!b) b = 0;
    // return a + (b || 0);
    return a + b;
}
console.log(add2(3, 4))
console.log(add2(3));


// 가변 파라미터 ES6 문법
function sum(...a) {
    let result = 0;
    for (let i = 0; i < a.length; ++i)
        result += a[i];
    return result;
}
console.log(sum(1, 2, 3, 4));
console.log(sum(4));
console.log(sum());

// 가변 파라미터 옛날 문법
// function sum() {
//     let result = 0;
//     for (let i = 0; i < arguments.length; ++i)
//         result += arguments[i];
//     return result;
// }
//
// console.log(sum(1, 2, 3, 4));
// console.log(sum(4));
// console.log(sum());

