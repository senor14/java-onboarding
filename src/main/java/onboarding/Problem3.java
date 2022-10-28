package onboarding;

/**
 * 🚀 기능 요구 사항
 * 배달이가 좋아하는 369게임을 하고자 한다. 놀이법은 1부터 숫자를 하나씩 대면서, 3, 6, 9가 들어가는 숫자는 숫자를 말하는 대신 3, 6, 9의 개수만큼 손뼉을 쳐야 한다.
 *
 * 숫자 number가 매개변수로 주어질 때, 1부터 number까지 손뼉을 몇 번 쳐야 하는지 횟수를 return 하도록 solution 메서드를 완성하라.
 *
 * 제한사항
 * number는 1 이상 10,000 이하인 자연수이다.
 */

public class Problem3 {
    private static final int[] numberArray = new int[10_001];
    public static int solution(int number) {
        boolean isAlreadyInitialized = numberArray[3] != 0;
        if (isAlreadyInitialized) {
            return numberArray[number];
        }
        initializeNumbers369Count();
        return numberArray[number];
    }

    private static void initializeNumbers369Count() {
        for (int i = 3; i < numberArray.length; i++) {
            String numStr = i + "";
            numberArray[i] = numberArray[i - 1] + getCount369Num(numStr);
        }
    }

    private static int getCount369Num(String numStr) {
        int count = 0;
        for (int j = 0; j < numStr.length(); j++) {
            int digitNumber = Character.getNumericValue(numStr.charAt(j));
            boolean isInclude369 = digitNumber % 3 == 0 && digitNumber != 0;
            if (isInclude369) {
                count++;
            }
        }
        return count;
    }
}
