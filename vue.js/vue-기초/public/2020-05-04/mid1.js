let a = [];
let sum = 0;

for(let i = 0; i < 100; ++i)
    sum += a.push(Math.floor(Math.random() * 100) + 1);

console.log((sum/100).toFixed(1));
