let i = 0;

for (;;) {
    ++i;
    if (i % 2 == 1) continue;
    console.log(i);
    if (i >= 20) break;
}