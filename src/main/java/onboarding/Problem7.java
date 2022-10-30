package onboarding;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 🚀 기능 요구 사항
 * 레벨 2의 팀 프로젝트 미션으로 SNS(Social Networking Service)를 만들고자 하는 팀이 있다.
 * 팀에 속한 크루 중 평소 알고리즘에 관심이 많은 미스터코는 친구 추천 알고리즘을 구현하고자 아래와 같은 규칙을 세웠다.
 *
 * 사용자와 함께 아는 친구의 수 = 10점
 * 사용자의 타임 라인에 방문한 횟수 = 1점
 * 사용자 아이디 user와 친구 관계 정보 friends, 사용자 타임 라인 방문 기록 visitors가 매개변수로 주어질 때,
 * 미스터코의 친구 추천 규칙에 따라 점수가 가장 높은 순으로 정렬하여 최대 5명을 return 하도록
 * solution 메서드를 완성하라.
 * 이때 추천 점수가 0점인 경우 추천하지 않으며, 추천 점수가 같은 경우는 이름순으로 정렬한다.
 *
 * 제한사항
 * user는 길이가 1 이상 30 이하인 문자열이다.
 * friends는 길이가 1 이상 10,000 이하인 리스트/배열이다.
 * friends의 각 원소는 길이가 2인 리스트/배열로 [아이디 A, 아이디 B] 순으로 들어있다.
 * A와 B는 친구라는 의미이다.
 * 아이디는 길이가 1 이상 30 이하인 문자열이다.
 * visitors는 길이가 0 이상 10,000 이하인 리스트/배열이다.
 * 사용자 아이디는 알파벳 소문자로만 이루어져 있다.
 * 동일한 친구 관계가 중복해서 주어지지 않는다.
 * 추천할 친구가 없는 경우는 주어지지 않는다.
 */

public class Problem7 {
    public static List<String> solution(String user, List<List<String>> friends, List<String> visitors) {
        PriorityQueue<Recommend> pq = new PriorityQueue<>();
        Map<String, List<String>> relationship = new HashMap<>();
        Map<String, Boolean> visited = new HashMap<>();
        for (List<String> friend : friends) {
            for (String nickname : friend) {
                if (!relationship.containsKey(nickname)) {
                    relationship.put(nickname, new ArrayList<>());
                    visited.put(nickname, false);
                }
            }
            relationship.get(friend.get(0)).add(friend.get(1));
            relationship.get(friend.get(1)).add(friend.get(0));
        }
        saveAcquaintanceScore(user, relationship, pq, visited);
        saveVisitScore(user, relationship, visitors, pq);
        return pq.stream()
                .sorted((o1, o2) -> {
                    if (o1.getScore() == o2.getScore()) {
                        return o1.getRecommendedUser().compareTo(o2.getRecommendedUser());
                    }
                    return o2.getScore() - o1.getScore();
                })
                .map(Recommend::getRecommendedUser)
                .limit(5)
                .collect(Collectors.toList());
    }

    private static void saveVisitScore(String user, Map<String, List<String>> relationship, List<String> visitors, PriorityQueue<Recommend> pq) {
        for (String visitor : visitors) {
            boolean isExist = false;
            for (Recommend recommend : pq) {
                if (recommend.recommendedUser.equals(visitor)) {
                    recommend.addScore(1);
                    isExist = true;
                    break;
                }
            }
            boolean isAlreadyFriend = false;
            for (String nickname : relationship.get(user)) {
                if (nickname.equals(visitor)) {
                    isAlreadyFriend = true;
                    break;
                }
            }
            if (!isExist && !isAlreadyFriend && !visitor.equals(user)) {
                pq.add(new Recommend(visitor, 1));
            }
        }
    }

    private static void saveAcquaintanceScore(String user, Map<String, List<String>> relationship, PriorityQueue<Recommend> pq, Map<String, Boolean> visited) {
        Queue<String> queue = new LinkedList<>();
        queue.add(user);
        visited.put(user, true);
        int level = 1;
        int nowSize = 1;
        int count = 0;
        Map<String, Recommend> recommendMap = new HashMap<>();
        while (level <= 2) {
            String nickname = queue.poll();
            for (String friend : relationship.get(nickname)) {
                if (!visited.get(friend)) {
                    queue.add(friend);
                    if (level == 2) {
                        if (recommendMap.containsKey(friend)) {
                            recommendMap.get(friend).addScore(10);
                            continue;
                        }
                        recommendMap.put(friend, new Recommend(friend, 10));
                    }
                }
            }
            count++;
            if (count == nowSize) {
                count = 0;
                nowSize = queue.size();
                level++;
            }
        }
        for (String key : recommendMap.keySet()) {
            pq.add(recommendMap.get(key));
        }
    }

    private static class Recommend implements Comparable<Recommend> {
        private final String recommendedUser;
        private int score;

        public Recommend(String recommendedUser, int score) {
            this.recommendedUser = recommendedUser;
            this.score = score;
        }

        public String getRecommendedUser() {
            return recommendedUser;
        }

        public int getScore() {
            return score;
        }

        public void addScore(int score) {
            this.score += score;
        }

        @Override
        public int compareTo(Recommend o) {
            if (this.getScore() == o.getScore()) {
                return this.getRecommendedUser().compareTo(o.getRecommendedUser());
            }
            return o.getScore() - this.getScore();
        }

        @Override
        public String toString() {
            return "Recommend{" +
                    "recommendedUser='" + recommendedUser + '\'' +
                    ", score=" + score +
                    '}';
        }

    }
}
