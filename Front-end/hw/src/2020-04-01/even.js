let a = [];

for(let i = 0; i < 100; ++i)
    a[i] = Math.floor(Math.random() * 100) + 1;

for(let i = 99; i >= 0; --i)
    if(a[i] % 2 == 0)
        a.splice(i, 1);

console.log(a);