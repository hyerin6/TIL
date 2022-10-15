function sum(a) {
    let n = 0;
    for(let i = 0; i < a.length; ++i)
        n += a[i];
    return n;
}

function test_sum(f) {
    let a = [];
    for(let i = 0; i < 5; ++i)
        a.push(Math.floor(Math.random() * 10) + 1);
    console.log(f(a));
}

for(let i = 0; i < 5; ++i)
    test_sum(sum);
