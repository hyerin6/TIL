-- hour 변수를 -1로 선언
SET @HOUR = -1;

-- hour이 +1 씩 증가하게 되면서 hour 조건에 맞는 서브 쿼리를 참조
-- 해당 hour(시간대)의 입양 건수를 구한다.
SELECT
    (@HOUR := @HOUR + 1) HOUR,
    (SELECT COUNT(*) FROM ANIMAL_OUTS WHERE HOUR(DATETIME) = @HOUR) COUNT
FROM ANIMAL_OUTS
WHERE @HOUR < 23;
