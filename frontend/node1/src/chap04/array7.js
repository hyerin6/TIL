// a
let a = [[1, 2, 3], [4, 5, 6], [7, 8, 9]];

for (let i = 0; i < a.length; ++i)
    for (let j = 0; j < a[i].length; ++j)
        console.log(a[i][j])

// b
let b = [];
    b[0] = [1, 2, 3];
    b[1] = [4, 5, 6];
    b[2] = [7, 8, 9];

    for (let i = 0; i < b.length; ++i)
        for (let j = 0; j < b[i].length; ++j)
            console.log(b[i][j]);

// c
let c1 = [1, 2, 3];
let c2 = [4, 5, 7];
let c3 = [7, 8, 9];
let c = [c1, c2, c3];

for (let i = 0; i < c.length; ++i)
    for (let j = 0; j < c[i].length; ++j)
        console.log(c[i][j])

