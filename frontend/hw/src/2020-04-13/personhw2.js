let persons = [
    { name: "홍길동", age: 16 },
    { name: "임꺽정", age: 18 },
    { name: "전우치", age: 19 }
];

let maxPerson = persons.reduce((p1, p2) => p1.age > p2.age ? p1 : p2);
console.log(maxPerson);
