import SuperHero.SuperHero;

public class Main {
    public static void main(String[] args) {
        SuperHero hero1 = new SuperHero();
        SuperHero hero2 = new SuperHero();

        hero1.SearchSuperHero("Spider-man");
        hero1.getPowerStatsObject("Spider-man");
        hero2.duel("Spider-man", "Deadpool");
    }
}