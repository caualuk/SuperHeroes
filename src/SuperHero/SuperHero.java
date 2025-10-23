package SuperHero;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SuperHero {
    private String name;
    private String fullName;
    private String height;
    private String weight;
    private String urlImage;


    public void SearchSuperHero(String name) {
        try {
            // Lowercase the name, then replace spaces with %20 for URL encoding
            String encodedName = name.toLowerCase().replace(" ", "%20");
            String url = "https://superheroapi.com/api/8ed705391e20f7fbac9335e38e7877d1/search/" + encodedName;

            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = buildRequest(url);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                getBiography(name);
            } else {
                System.out.println("Erro na requisição do código: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro na requisição: " + e.getMessage());
        }
    }

    private void getBiography(String name) {
        try {
            // Lowercase the name, then replace spaces with %20 for URL encoding
            String encodedName = name.toLowerCase().replace(" ", "%20");
            String url = "https://superheroapi.com/api/8ed705391e20f7fbac9335e38e7877d1/search/" + encodedName;

            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = buildRequest(url);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = new JsonParser().parseString(response.body()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");
                JsonObject firstResult = results.get(0).getAsJsonObject();

                name = firstResult.get("name").getAsString();

                JsonObject biography = firstResult.getAsJsonObject("biography");

                String fullName = biography.get("full-name").getAsString();
                String placeOfBirth = biography.get("place-of-birth").getAsString();
                String alignment = biography.get("alignment").getAsString();

                showBiography(name, fullName, placeOfBirth, alignment);

            } else {
                System.out.println("Erro na requisição do código: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro na requisição: " + e.getMessage());
        }

    }

//    public void getPowerStats(String name) {
//        try {
//            // Lowercase the name, then replace spaces with %20 for URL encoding
//            String encodedName = name.toLowerCase().replace(" ", "%20");
//            String url = "https://superheroapi.com/api/8ed705391e20f7fbac9335e38e7877d1/search/" + encodedName;
//
//            HttpClient client = HttpClient.newBuilder()
//                    .followRedirects(HttpClient.Redirect.ALWAYS)
//                    .build();
//
//            HttpRequest request = buildRequest(url);
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200) {
//                JsonObject jsonObject = new JsonParser().parseString(response.body()).getAsJsonObject();
//                JsonArray results = jsonObject.getAsJsonArray("results");
//                JsonObject firstResult = results.get(0).getAsJsonObject();
//
//                JsonObject powerstats = firstResult.getAsJsonObject("powerstats");
//
//                String intelligence = powerstats.get("intelligence").getAsString();
//                String strength = powerstats.get("strength").getAsString();
//                String speed = powerstats.get("speed").getAsString();
//                String durability = powerstats.get("durability").getAsString();
//                String power = powerstats.get("power").getAsString();
//                String combat = powerstats.get("combat").getAsString();
//
//                showPowerStats(name, intelligence, strength, speed, durability, power, combat);
//
//            } else {
//                System.out.println("Erro na requisição do código: " + response.statusCode());
//            }
//        } catch (IOException | InterruptedException e) {
//            System.out.println("Erro na requisição: " + e.getMessage());
//        }
//    }

    public class PowerStats {
        int intelligence;
        int strength;
        int speed;
        int durability;
        int power;
        int combat;

        int total() {
            return intelligence + strength + speed + durability + power + combat;
        }
    }


    public PowerStats getPowerStatsObject(String name) {
        try {
            String encodedName = name.toLowerCase().replace(" ", "%20");
            String url = "https://superheroapi.com/api/8ed705391e20f7fbac9335e38e7877d1/search/" + encodedName;

            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = buildRequest(url);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");
                JsonObject firstResult = results.get(0).getAsJsonObject();

                JsonObject powerstats = firstResult.getAsJsonObject("powerstats");

                PowerStats stats = new PowerStats();
                stats.intelligence = parseIntSafe(powerstats.get("intelligence").getAsString());
                stats.strength = parseIntSafe(powerstats.get("strength").getAsString());
                stats.speed = parseIntSafe(powerstats.get("speed").getAsString());
                stats.durability = parseIntSafe(powerstats.get("durability").getAsString());
                stats.power = parseIntSafe(powerstats.get("power").getAsString());
                stats.combat = parseIntSafe(powerstats.get("combat").getAsString());

                return stats;
            } else {
                System.out.println("Erro na requisição do código: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro na requisição: " + e.getMessage());
        }
        return null;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public void duel(String hero1, String hero2) {
        PowerStats stats1 = getPowerStatsObject(hero1);
        PowerStats stats2 = getPowerStatsObject(hero2);

        if(stats1 == null && stats2 == null) {
            System.out.println("Não foi possível buscar os heróis para o duelo.");
            return;
        }

        int total1 = stats1.total();
        int total2 = stats2.total();

        System.out.println("\n⚔️ DUEL: " + hero1 + " vs " + hero2 + "\n");
        System.out.println(hero1 + " - Total de poder: " + total1);
        System.out.println(hero2 + " - Total de poder: " + total2);

        if (total1 > total2) {
            System.out.println("\n🏆 " + hero1 + " venceu o duelo!");
        } else if (total1 < total2) {
        } else if (total1 < total2) {
            System.out.println("\n🏆 " + hero2 + " venceu o duelo!");
        } else {
            System.out.println("\n🤝 Empate técnico!");
        }
    }

    public HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
    }

    private void showBiography(String name, String fullName, String placeOfBirth, String alignment) {
        System.out.println("=== Biografia do Herói ===\n");
        System.out.println("Nome: " + name);
        if(!Objects.equals(fullName, "")) {
            System.out.println("Nome completo: " + fullName);
        } else{
            System.out.println("Nome completo: " + name);
        }
        System.out.println("Local de nascimento: " + placeOfBirth);
        System.out.println("Temperamento: " + alignment + "\n");
        System.out.println("==========================");
    }

    private void showPowerStats(String name, String intelligence, String strength, String speed, String durability, String power, String combat) {
        System.out.println("=== Poder do héroi ===\n");
        System.out.println("Nome completo: " + name);
        System.out.println("Inteligência: " + intelligence);
        System.out.println("Força: " + strength);
        System.out.println("Velocidade: " + speed);
        System.out.println("Durabilidade: " + durability);
        System.out.println("Poder: " + power);
        System.out.println("Combate: " + combat);
        System.out.println("==========================");
    }
}
