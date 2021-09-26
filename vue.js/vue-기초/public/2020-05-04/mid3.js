let persons1 = [];

let p = {
    name: "홍길동",
    age: 16,
    re : function(i) { return { name: this.name, age: this.age + i } }
};

for(let i = 0; i < 3; ++i)
    persons1[i] = p.re(i);

let persons2 = persons1;

persons1[0].age = 20;

console.log(persons1);
console.log(persons2);




