let persons1 = [];
let persons2 = [];

for(let i = 0; i < 10; ++i)
    persons1[i] = { name: "홍길동", age: 16 + i };

function cloneObject(obj) { // deep copy
    let clone = [];
    for(let i in obj) {
        if(typeof(obj[i]) == "object" && obj[i] != null)
            clone[i] = cloneObject(obj[i]);
        else
            clone[i] = obj[i];
    }
    return clone;
}

persons2 = cloneObject(persons1);
console.log(persons2);
