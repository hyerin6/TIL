let a = [];

for(let i = 0; i < 100; ++i)
    a.push(Math.floor(Math.random() * 100) + 1);

a = a.filter((e) => e % 2 === 0)
let sum = a.reduce((a, b) => a + b);

console.log(sum);






