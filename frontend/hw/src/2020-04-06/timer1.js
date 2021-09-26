let count = 0;
let id = setInterval(() => {
    console.log(new Date());
    count++;
    if(count == 10) clearInterval(id);
    }, 1000);
