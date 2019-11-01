import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class App {

    //Took the Ponies Names from Google. Not really a big fan of MLP so i'm sorry if i left out your favourite
    private static final String[] PONIES_NAMES = {
            "Twilight Sparkle",
            "Pinkie Pie",
            "Rainbow Dash",
            "Fluttershy",
            "Rarity",
            "Applejack",
            "Princess Luna",
            "Princess Celestia",
            "Spike"
    };

    private static final int MAZE_WIDTH = 15;
    private static final int MAZE_HEIGHT = 25;

    private final OkHttpClient httpClient = new OkHttpClient();

    public static void main(String[] args) throws Exception {

        App obj = new App();

        String id = obj.createMaze();
        System.out.println("Maze created");
        obj.getMaze(id);
        obj.printMaze(id);
        cleanConsole();
    }

    private void getMaze(String id) throws Exception {

        Request request = new Request.Builder()
                .url("https://ponychallenge.trustpilot.com/pony-challenge/maze/" + id)
                .addHeader("custom-key", "mkyong")  // add request headers
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            System.out.println(response.body().string());
        }

    }

    private void printMaze(String id) throws Exception {

        Request request = new Request.Builder()
                .url("https://ponychallenge.trustpilot.com/pony-challenge/maze/" + id + "/print")
                .addHeader("custom-key", "mkyong")  // add request headers
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            System.out.println(response.body().string());
        }

    }

    private String createMaze() throws Exception {
        // json request body
        RequestBody body = RequestBody.create(
                createMazeJSONRequest(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://ponychallenge.trustpilot.com/pony-challenge/maze\n")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            JSONObject obj = new JSONObject(response.body().string());
            String mazeId = obj.getString("maze_id");
            response.close();
            System.out.println(mazeId);
            return mazeId;
        }

    }

    private void makeMovement(String id) throws Exception {

        RequestBody body = RequestBody.create(
                createMovementJSONRequest(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://ponychallenge.trustpilot.com/pony-challenge/maze/" + id)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "OkHttp Bot")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            System.out.println(response.body().string());
        }

    }

    private String createMovementJSONRequest() {
          /*{
                "direction": "string"
            }*/
    }

    private String getRandomPony(){ //Never thought i'd name a function like this
        Random r = new Random();
        int randomNumber = r.nextInt(PONIES_NAMES.length);
        return PONIES_NAMES[randomNumber];
    }

    private String createMazeJSONRequest(){
        /*{
            "maze-width": 0,
            "maze-height": 0,
            "maze-player-name": "string",
            "difficulty": 0
        }*/

        String json = new JSONObject()
                .put("maze-width", MAZE_WIDTH)
                .put("maze-height", MAZE_HEIGHT)
                .put("maze-player-name", getRandomPony())
                .put("difficulty", 1).toString();

        return json;
    }

    private static void cleanConsole(){
        String os = getOs();
        switch (os){
            case "Linux":{
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
    }

    private static String getOs(){
        return System.getProperty("os.name");
    }



}
