let a = [3, 1, 6, 2, 4, 8, 10, 5, 11, 7, 9];
a.sort(compareNumber);
console.log(a);

function compareNumber(i, j) {
    return j - i;
}