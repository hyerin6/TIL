/** 10 이상 20 이하의 정수를 임의로 30개 출력하는 코드를 구현하시오.
 *  Math.random() * (MAX - MIN + 1) + MIN
 *  이 계산식의 값은 MIN 보다 크거나 같고, MAX 보다 작거나 같은 정수들 중의 하나이다.
 *  solution code : console.log(Math.floor(Math.random() * (20-10+1)) + 10);
 */

// Math.floor(Math.random() * 11)의 범위가 0 ~ 10 이기 때문에 10을 더해준다.
for(let i = 0; i < 30; i++)
    console.log(Math.floor(Math.random() * 11) + 10);
