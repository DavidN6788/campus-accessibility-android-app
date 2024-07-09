package com.example.campus_accessability_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapFrontend {

    // Holds the only instance(lobby) of this class
    private static final MapFrontend instance = new MapFrontend();
    // Private constructor so no one can create a lobby
    public MapFrontend(){
    }
    // Global access point to get the instance of the singleton class
    public static MapFrontend getInstance(){ return instance; }

    private BackendAPI backendapi;
    private GoogleMap map;
    public static ArrayList<Node> nodes = new ArrayList<>();
    public GraphEdge[][] graphMatrix; // Stores GraphEdge object for each edge
    public HashMap<Polyline, GraphEdge> polylineGraphEdgeHashMap = new HashMap<>();
    protected static boolean ttsEnabled = false;

    private final static int NODE_COUNT = 30;
    public final static String[] nodeNames = {"West Car Park Entrance", "Polden Court", "10 West Entrance", "Medical & Dental Centre", "The Lodge", // 1-5
            "Westwood", "5, 7, 9 West", "Brendon Court", "The Avenue (South)", "South Buildings", // 6-10
            "University Lake", "Milner Centre", "South Car Park", "Sports Training Village", "Parcel Office", // 11-15
            "Eastwood & Marlborough Court", "Solsbury Court & The Quads", "East Car Park", "East Building", "The Edge", // 16-20
            "Arrivals Square", "University Hall", "8 West", "4 West", "Library", // 21-25
            "2, 4, 6, 8 East", "Students' Union & Norwood House", "The Chancellors' Building", "Lime Tree", "1, 3 East"}; // 26-30

    private Double userLat;
    private Double userLong;

    private ArrayList<Circle> googleMapNodes = new ArrayList<>();
    private ArrayList<Polyline> googleMapEdges = new ArrayList<>();
    private ArrayList<String> currentDirections = new ArrayList<>();
    private int instructionDisplayedNum;

    private ArrayList<Circle> buildingButtons = new ArrayList<>();

    private Activity homeActivity;
    private ScrollView instructionsScroll;
    private TextView instructionsTextView;
    private ImageButton prevInstruction;
    private ImageButton nextInstruction;
    private View journeyDetails;
    private TextView routeDuration;
    private TextView routeDistance;
    private AppCompatButton exitButton;
    private FrameLayout buildingInfoWindow;
    private TextView buildingText;
    private ImageButton closeBuildInfo;

    public MapFrontend(GoogleMap googleMap) {
        map = googleMap;
        homeActivity = (Activity) HomeActivity.getAppContext();
        backendapi = new BackendAPI(homeActivity);
        instructionsTextView = homeActivity.findViewById(R.id.instructions_text);
        instructionsScroll = homeActivity.findViewById(R.id.scroll_instructions);
        prevInstruction = homeActivity.findViewById(R.id.prev_instruction);
        nextInstruction = homeActivity.findViewById(R.id.next_instruction);

        nextInstruction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showNextInstruction(); }});
        prevInstruction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { showPrevInstruction(); }});

        // Bottom journey details view:
        journeyDetails = homeActivity.findViewById(R.id.journey_details);
        routeDuration = homeActivity.findViewById(R.id.time_text);
        routeDistance = homeActivity.findViewById(R.id.distance_text);
        exitButton = homeActivity.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { exitJourney(); }});

        buildingInfoWindow = homeActivity.findViewById(R.id.building_info);
        buildingText = homeActivity.findViewById(R.id.building_text);
        closeBuildInfo = homeActivity.findViewById(R.id.close_build_info);
        closeBuildInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { hideBuildingInfoWindow(); }});
    }

    private void exitJourney() {
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearMapObjects();
                hideDetailsView();
                hideInstructionViews();
            }
        });
        HomeActivity.routeDisplayed = false;
    }

    protected void showDetailsView() {
        journeyDetails.setVisibility(View.VISIBLE);
    }

    protected void hideDetailsView() {
        journeyDetails.setVisibility(View.GONE);
    }

    private void setJourneyDetails(Double mins, Integer metres) {
        String displayMins;
        if (mins < 1) {
            displayMins = "1"; // round up
        } else {
            displayMins = String.valueOf(Math.round(mins));
        }
        String timeText = displayMins+" minute journey";
        String distanceText = metres.toString()+" metres";
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                routeDuration.setText(timeText);
                routeDistance.setText(distanceText);
                showDetailsView();
            }
        });
    }

    private void showNextInstruction() {
        if (instructionDisplayedNum + 1 > currentDirections.size() - 1) {
            return; // does not make sense to go from final instruction to first instruction, so do nothing.
        } else {
            instructionDisplayedNum += 1;
        }
        instructionsTextView.setText(currentDirections.get(instructionDisplayedNum));
        if(ttsEnabled){
            HomeActivity.t1.speak(instructionsTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

        }
    }

    private void showPrevInstruction() {
        if (instructionDisplayedNum - 1 < 0) {
            return; // does not make sense to go from first instruction to final instruction, so do nothing.
        } else {
            instructionDisplayedNum -= 1;
        }
        instructionsTextView.setText(currentDirections.get(instructionDisplayedNum));
        if(ttsEnabled){
            HomeActivity.t1.speak(instructionsTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

        }
    }

    protected void hideInstructionViews() {
        instructionsScroll.setVisibility(View.GONE);
        prevInstruction.setVisibility(View.GONE);
        nextInstruction.setVisibility(View.GONE);
    }

    protected void showInstructionViews() {
        instructionsScroll.setVisibility(View.VISIBLE);
        prevInstruction.setVisibility(View.VISIBLE);
        nextInstruction.setVisibility(View.VISIBLE);
    }

    private void drawNode(LatLng centreLatLng) {
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(centreLatLng)
                .radius(5) // In meters
                .strokeColor(0xffff0000)
                .fillColor(0xffff0000) // Colour is in ARGB format: https://stackoverflow.com/questions/20442653/any-one-tell-me-why-we-write-this-0xff000000-in-filters
                .zIndex(1); // display above map Ground Overlay
        // blue: 0xff587cd1

        // Get back the mutable Circle
        Circle circle = map.addCircle(circleOptions);
        googleMapNodes.add(circle);
    }

    private void drawAllNodes() {
        for (Node node : nodes) {
            if (node != null) { // node.get(0) is null, as there is no node 0
                drawNode(node.coordinatesLatLng);
            }
        }
    }

    /** Iterates through 2D array of coordinates to store them in a dictionary as LatLng types.
     * rawCoordinates store nodes in chronological order, e.g. index 0 = node 1. **/
    private void setUpGraphNodes() {
        double[][] rawValues = {{51.3804637064681, -2.3331981300481828}, {51.38098150514017, -2.3321909898831374}, {51.38003619991349, -2.33151889216109}, {51.3790946392696, -2.3339059882448274}, {51.37845346831037, -2.3327316764579096}, // 1-5
                {51.380880879314574, -2.3310878084352074}, {51.38074287303462, -2.329860626575527}, {51.38059336576304, -2.328091715822725}, {51.37730424433999, -2.3306905460931415}, {51.37844248529428, -2.3295918493050727}, // 6-10
                {51.378033739107316, -2.327934458152407}, {51.37720513801012, -2.3281295232019845}, {51.37701651418047, -2.326076849717547}, {51.377856978735686, -2.325072940681827}, {51.38063250092618, -2.325485287556707}, // 11-15
                {51.380970847550444, -2.32354316677547}, {51.38030185322843, -2.3231305324794405},  {51.379286354264295, -2.322475113345254}, {51.37890920936486, -2.323479632510561}, {51.37858531048867, -2.324795332630786}, // 16-20
                {51.37914914004649, -2.3255072602098052}, {51.379121490579, -2.3283531663533514}, {51.37974917917565, -2.3301913590699663}, {51.37971933137766, -2.329122949536168}, {51.37963285180883, -2.3280854824400525}, // 21-25
                {51.37955199190126, -2.327249810462711}, {51.37951036369387, -2.3269106209649544}, {51.3800525910002, -2.3256580727047034}, {51.3798430488762, -2.3249942893347226}, {51.38005711606455, -2.3272683403204755}}; // 26-30

        nodes.add(null); // placeholder, for readability - so that nodes.get(1) is actually node 1

        Node tempNode;
        for (int i = 0; i < rawValues.length; i++) {
            tempNode = new Node(i+1, new LatLng(rawValues[i][0], rawValues[i][1]), nodeNames[i]);
            nodes.add(tempNode);
        }
    }

    private void setUpEdge(double[][] pointsArray, Node nodeA, Node nodeB) {
        ArrayList<LatLng> pointsLatLng = new ArrayList<>();
        pointsLatLng.add(nodeA.coordinatesLatLng); // add start/end node - NODE A

        for (double[] points : pointsArray) {
            pointsLatLng.add(new LatLng(points[0], points[1]));
        }

        pointsLatLng.add(nodeB.coordinatesLatLng); // add start/end node - NODE B
        GraphEdge edge = new GraphEdge(nodeA, nodeB, pointsLatLng);

        graphMatrix[nodeA.nodeNumber][nodeB.nodeNumber] = edge;
        graphMatrix[nodeB.nodeNumber][nodeA.nodeNumber] = edge;
    }

    private void setUpGraphEdges() {
        graphMatrix = new GraphEdge[NODE_COUNT + 1][NODE_COUNT + 1];

        // Edge connected by vertices 1:2
        double[][] points = {{51.38038874506884, -2.332634495002602}, {51.38026406808317, -2.3321356533427062}, {51.38094172887468, -2.331861504529572}};
        setUpEdge(points, nodes.get(1), nodes.get(2)); // Edge between nodes/vertices 1 and 2
        // 1 <--> 3
        points = new double[][]{{51.38038874506884, -2.332634495002602}, {51.38013700616999, -2.3317603485278737}};
        setUpEdge(points, nodes.get(1), nodes.get(3));
        // 1 <--> 4
        points = new double[][]{{51.38037550845161, -2.3336678593120594}, {51.38021690521332, -2.333998836404833}, {51.379591885589726, -2.334943719121389}};
        setUpEdge(points, nodes.get(1), nodes.get(4));
        // 2 <--> 3
        points = new double[][]{{51.380917392219544, -2.3314979985645006}, {51.38014957578242, -2.3317150418121835}};
        setUpEdge(points, nodes.get(2), nodes.get(3));
        // 2 <--> 6
        points = new double[][]{};
        setUpEdge(points, nodes.get(2), nodes.get(6));
        // 3 <--> 10
        points = new double[][]{{51.379801481345005, -2.3317103532517183}, {51.379656997360044, -2.3315109703143544}, {51.379397485005065, -2.3315884337213593}, {51.379414656869045, -2.330676545474354}, {51.37953823107352, -2.3301528432682552}, {51.37910239797471, -2.330212451655823}, {51.378854508879414, -2.3301093496863743}, {51.378708967246034, -2.3297105838325955}};
        setUpEdge(points, nodes.get(3), nodes.get(10));
        // 3 <--> 23
        points = new double[][]{{51.37991569153503, -2.3313944454093325}, {51.37983975858909, -2.3307861585090373}};
        setUpEdge(points, nodes.get(3), nodes.get(23));
        // 4 <--> 5
        points = new double[][]{};
        setUpEdge(points, nodes.get(4), nodes.get(5));
        // 5 <--> 9
        points = new double[][]{{51.37812324223984, -2.3321708438503452}, {51.37770469637656, -2.3315593001667247}};
        setUpEdge(points, nodes.get(5), nodes.get(9));
        // 6 <--> 7
        points = new double[][]{};
        setUpEdge(points, nodes.get(6), nodes.get(7));
        // 7 <--> 8
        points = new double[][]{};
        setUpEdge(points, nodes.get(7), nodes.get(8));
        // 8 <--> 15
        points = new double[][]{{51.38057590161944, -2.32647778340826}};
        setUpEdge(points, nodes.get(8), nodes.get(15));
        // 8 <--> 25
        points = new double[][]{{51.37967485254918, -2.3283454869998277}};
        setUpEdge(points, nodes.get(8), nodes.get(25));
        // 8 <--> 30
        points = new double[][]{{51.38015725461226, -2.3275877961375855}};
        setUpEdge(points, nodes.get(8), nodes.get(30));
        // 9 <--> 10
        points = new double[][]{};
        setUpEdge(points, nodes.get(9), nodes.get(10));
        // 10 <--> 11
        points = new double[][]{{51.37823917752471, -2.328924544485695}, {51.37799871397126, -2.3284750880429543}, {51.37794808990252, -2.3281946001985108}};
        setUpEdge(points, nodes.get(10), nodes.get(11));
        // 10 <--> 22
        points = new double[][]{{51.378372064747815, -2.329299654759712}, {51.379137740688265, -2.32905296061348}};
        setUpEdge(points, nodes.get(10), nodes.get(22));
        // 11 <--> 12
        points = new double[][]{};
        setUpEdge(points, nodes.get(11), nodes.get(12));
        // 11 <--> 13
        points = new double[][]{{51.37790955210316, -2.3279637927366768}, {51.37750241692637, -2.327182825783683}, {51.377296109373475, -2.326460358241445}};
        setUpEdge(points, nodes.get(11), nodes.get(13));
        // 11 <--> 22
        points = new double[][]{{51.37881304751054, -2.3277731836834747}, {51.37886966104536, -2.3281188472696543}, {51.37906407533851, -2.328129857793779}};
        setUpEdge(points, nodes.get(11), nodes.get(22));
        // 12 <--> 13
        points = new double[][]{};
        setUpEdge(points, nodes.get(12), nodes.get(13));
        // 13 <--> 14
        points = new double[][]{{51.376946801251435, -2.3254319874816067}, {51.37698600166956, -2.325306384103449}, {51.37669631642852, -2.324620887722126}, {51.376795978988, -2.324559150468455}, {51.376966420585504, -2.3249872456850498}, {51.377124633919195, -2.3252541254947916}};
        setUpEdge(points, nodes.get(13), nodes.get(14));
        // 14 <--> 20
        points = new double[][]{{51.37855021575506, -2.3249097278009168}};
        setUpEdge(points, nodes.get(14), nodes.get(20));
        // 15 <--> 16
        points = new double[][]{{51.3806366193198, -2.324972832356106}};
        setUpEdge(points, nodes.get(15), nodes.get(16));
        // 15 <--> 28
        points = new double[][]{};
        setUpEdge(points, nodes.get(15), nodes.get(28));
        // 16 <--> 17
        points = new double[][]{};
        setUpEdge(points, nodes.get(16), nodes.get(17));
        // 17 <--> 18
        points = new double[][]{};
        setUpEdge(points, nodes.get(17), nodes.get(18));
        // 17 <--> 29
        points = new double[][]{};
        setUpEdge(points, nodes.get(17), nodes.get(29));
        // 18 <--> 19
        points = new double[][]{};
        setUpEdge(points, nodes.get(18), nodes.get(19));
        // 19 <--> 20
        points = new double[][]{};
        setUpEdge(points, nodes.get(19), nodes.get(20));
        // 20 <--> 21
        points = new double[][]{{51.3786273674587, -2.3248865997909625}, {51.379143867611354, -2.324761750342945}, {51.37916479433632, -2.32491262460488}, {51.37919787820534, -2.325072811614213}, {51.37923043288625, -2.325441106260848}};
        setUpEdge(points, nodes.get(20), nodes.get(21));
        // 21 <--> 27
        points = new double[][]{{51.37933433476536, -2.3262204264157207}, {51.3794049491656, -2.3266808814148603}};
        setUpEdge(points, nodes.get(21), nodes.get(27));
        // 21 <--> 29
        points = new double[][]{{51.37928954383727, -2.325529039354684}, {51.37974773793408, -2.325416390995849}};
        setUpEdge(points, nodes.get(21), nodes.get(29));
        // 22 <--> 24 TODO: CONFIRM ACCURACY
        points = new double[][]{{51.37921072858822, -2.3286533781614644}, {51.3795601918164, -2.3286299605778678}};
        setUpEdge(points, nodes.get(22), nodes.get(24));
        // 22 <--> 25
        points = new double[][]{{51.37930854876468, -2.328028086570141}, {51.37948195157422, -2.3279833802869856}};
        setUpEdge(points, nodes.get(22), nodes.get(25));
        // 23 <--> 24
        points = new double[][]{{51.37970518053685, -2.3295542922739285}, {51.37973707048123, -2.3294191089854746}};
        setUpEdge(points, nodes.get(23), nodes.get(24));
        // 24 <--> 25
        points = new double[][]{};
        setUpEdge(points, nodes.get(24), nodes.get(25));
        // 25 <--> 26
        points = new double[][]{};
        setUpEdge(points, nodes.get(25), nodes.get(26));
        // 25 <--> 30
        points = new double[][]{{51.37962794313386, -2.327700123131898}, {51.38006877630724, -2.3275843796612365}};
        setUpEdge(points, nodes.get(25), nodes.get(30));
        // 26 <--> 27
        points = new double[][]{};
        setUpEdge(points, nodes.get(26), nodes.get(27));
        // 28 <--> 29
        points = new double[][]{{51.379906384110136, -2.325654332163472}, {51.37976763599336, -2.325423259560189}};
        setUpEdge(points, nodes.get(28), nodes.get(29));
    }

    /** Draws graph edge based on point coordinates.
     * NOTE: They must be in the correct order to draw a correct line. */
    private void drawEdge(GraphEdge edge) {
        if (edge == null) {
            Toast.makeText(MainActivity.getContext(), "An error occurred while drawing the route. (Error: edge does not exist.)", Toast.LENGTH_LONG).show();
            return;
        }
        PolylineOptions lineOptions = new PolylineOptions()
                .color(0xffff0000)
                .width(5);

        for (LatLng point : edge.getEdgePoints()) {
            lineOptions.add(point);
        }
        Polyline line = map.addPolyline(lineOptions);
        line.setClickable(true);
        polylineGraphEdgeHashMap.put(line, edge);
        googleMapEdges.add(line);
    }

    private void drawAllGraphEdges() {
        for (int row = 1; row <= NODE_COUNT; row++) { // not concerned with row 0 - no node 0
            for (int column = 1; column <= NODE_COUNT; column++) {

                if (graphMatrix[row][column] != null) {
                    drawEdge(graphMatrix[row][column]);
                }
            }
        }
    }

    protected void drawRoute(int[] routeArray) {
        int index;
        GraphEdge edge;
        // Iterate through each index, apart from the last:
        for (index = 0; index < routeArray.length-1; index++) {
            //Log.d("DEBUG_EDGE", String.valueOf(routeArray[index])+", "+String.valueOf(routeArray[index+1])); // If an edge is not successfully drawn, this can be used to debug it
            // Draw edge:
            edge = graphMatrix[routeArray[index]][routeArray[index+1]];
            drawEdge(edge);
            // Draw node at [index]:
            drawNode(nodes.get(routeArray[index]).coordinatesLatLng);
        }
        drawNode(nodes.get(routeArray[index]).coordinatesLatLng); // draw final node in array
    }

    /**
     * Deletes Google Map objects (lines and circles from routes, but not the map overlay).
     */
    protected void clearMapObjects() {
        for (Circle googleMapNode : googleMapNodes) {
            googleMapNode.remove();
        }
        for (Polyline googleMapEdge : googleMapEdges) {
            googleMapEdge.remove();
        }
    }

    protected void hideBuildingInfoWindow() {
        buildingInfoWindow.setVisibility(View.GONE);
    }

    private void displayBuildingInfoWindow(String buildingName) {
        buildingInfoWindow.setVisibility(View.VISIBLE);

        Building building = BackendAPI.buildings.get(buildingName);

        StringBuilder result = new StringBuilder();
        result.append("\tBuilding: "+buildingName+"\n\n\tEntrances");
        assert building != null;
        int count = 1;
        for (Entrance entrance: building.entrances) {
            result.append("\n\t\t\tDoor "+count+": ").append(entrance.doorautomatic);
            result.append("\n\t\t\tDoor width: ").append(entrance.doorwidth);
            result.append("\n\t\t\tDoor location: ").append(entrance.location);
            result.append("\n\t\t\tDoor steps: ").append(entrance.steps);
            result.append("\n");
            count += 1;
        }

        if (building.lifts.length == 0) {
            result.append("\n\tNo lifts\n");
        } else {
            result.append("\n\tLifts");
            for (Lift lift: building.lifts) {
                result.append("\n\t\t\tLift dimensions: ").append(lift.dimensions);
                result.append("\n\t\t\tLift location: ").append(lift.location);
                result.append("\n");
            }
        }

        if (building.toiletLocations.length == 0) {
            result.append("\n\tNo toilets\n");
        } else {
            result.append("\n\tToilets");
            count = 1;
            for (String toiletLocation: building.toiletLocations) {
                result.append("\n\t\t\tToilet "+count+": ").append(toiletLocation);
                count += 1;
            }
            //Log.e("myapp", result.toString());
            buildingText.setText(result);
        }
    }

    private void drawBuildingButton(LatLng centreLatLng, String buildingName) {
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(centreLatLng)
                .radius(12) // In meters
                .clickable(true)
                .strokeColor(0x33ff0000)
                .fillColor(0x1Aff0000) // Colour is in ARGB format: https://stackoverflow.com/questions/20442653/any-one-tell-me-why-we-write-this-0xff000000-in-filters
                .zIndex(1); // display above map Ground Overlay
        // blue: 0xff587cd1

        // Get back the mutable Circle
        Circle circle = map.addCircle(circleOptions);
        //buildingButtons.add(circle);
        circle.setTag(buildingName);

        map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                displayBuildingInfoWindow((String) circle.getTag());
            }
        });
    }

    private void setUpBuildingButtons() {
        // Cooordinates assume buildings (https://github.bath.ac.uk/hajw21/campus_accessibility_app/blob/master/backend/Building_Desc.py) are in alphabetical order in hashmap
        // (10W, 1E, 1S, 1W, 1WN, 2E, 2S, etc.)
        // Can verify with:
//        for (String key : BackendAPI.buildings.keySet()) {
//            Log.v("building_names", key);
//        }

        double[][] rawValues = {{51.37957832343159, -2.3318962449355225}, {51.37979926187721, -2.327404811086245802}, {51.378039885306414, -2.3307135525199296}, {51.379916949578956, -2.32868677212402}, {51.38044573783916, -2.3285938979234144}, // 10W, 1E, 1S, 1W, 1WN
                {51.37942385923509, -2.3276204746469134}, {51.37909485716808, -2.3277465042305034}, {51.37954539633059, -2.3284152907527957}, // 2E, 2S, 2W
                {51.38029853217511, -2.3272416846873427}, {51.37758807527754, -2.329909576243935}, {51.380032839708605, -2.32933992677537}, {51.380489607294436, -2.3292633382698527}, // 3E, 3S, 3W, 3WN
                {51.379363717450936, -2.3268527843443607}, {51.37859487111726, -2.3268876343539278}, {51.37751330062177, -2.3292831554767814}, {51.37709886198394, -2.328763128228687}, {51.37957050726574, -2.3291493362944466}, // 4E, 4ES, 4S, 4SA, 4W
                {51.380121777507966, -2.330718923964432}, // 5W
                {51.3791560942287, -2.3259713294565376}, {51.3792763756547, -2.329869057470253}, {51.3789714901599, -2.329866132488563}, // 6E, 6W, 6WS
                {51.38067307138262, -2.3313027408713785}, // 7W
                {51.37884804225704, -2.326336221090401}, {51.37979922864059, -2.331039334802794}, // 8E, 8W
                {51.38034267930385, -2.331372579331705}, // 9W
                {51.380210431233145, -2.3289612103912005}, {0.0, 0.0}, // biomechanics suite, balehouse??
                {51.38016455614805, -2.325916558171943}, {51.379449133760666, -2.3283991276576876}, // chancellors'. claverton rooms restaurant
                {51.38044074337667, -2.3260452659235784}, // department of estates
                {51.37878880110478, -2.323175202693087}, // east building
                {51.37964693099058, -2.3260740378689686}, {51.37969694106011, -2.326064675456493}, {51.37953676516371, -2.328260837855898}, // founders hall, founders sports hall??, fountain canteen
                {51.379829398274694, -2.3279923650036456}, {51.37971220791056, -2.3247882201012144}, // library, lime tree
                {51.38054590445886, -2.3246203394753686}, {51.37876008408788, -2.3340848812286237}, // malborough court, medical centre
                {51.379537016932474, -2.327109290120062}, // norwood
                {51.37930260077105, -2.3285431832856176}, {51.38086424130178, -2.3257634456694785}, {51.379606135849386, -2.3269207650884556}, {51.380542432071806, -2.3325749800135243}, {51.380914819354174, -2.332428722368082}, // parade bar, parcel office, plug n tub, polden court
                {51.3805019429158, -2.324212094672703}, {51.37780894607644, -2.3246923004186626}, {51.37969792870693, -2.326819160749651}, // solsbury, STV, SU
                {51.37849223939205, -2.3245169359496667}, {51.37983942985755, -2.323822388761824}, // the edge, the quads
                {51.38005935602082, -2.3298966558572527}, // wessex house
                {51.381155264702855, -2.330684940145591}, {51.38129310796676, -2.329431189778151}, {51.38095198488596, -2.3295111161248307}, {51.381240436543955, -2.3300750295165926}, {51.3810962100564, -2.328587839481897}, // westwood buildings
                {51.380712336129015, -2.3230240942406866} // woodland court
        };

        LatLng buttonCoordinates;
        int buildingNumber = 0;
        for (String buildingName : BackendAPI.buildings.keySet()) {
            buttonCoordinates = new LatLng(rawValues[buildingNumber][0], rawValues[buildingNumber][1]);
            drawBuildingButton(buttonCoordinates, buildingName);
            buildingNumber += 1;
        }
    }

    @SuppressLint("MissingPermission")
    protected void setUpMap() {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID); // Satellite map

        LatLng campus = new LatLng(51.37785429353723, -2.326649171510055);
//        map.moveCamera(CameraUpdateFactory.newLatLng(campus)); // Alternative for declaring coordinates

        // opaque_map_3 (opacity 23) for testing default, abstract Google Map
        // opaque_map (opacity 159) for when using satellite imagery, maybe opaque_map_2 (opacity 80)
        GroundOverlayOptions mapOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.opaque_map))
                .position(campus, 1500f, 1500f);

        map.addGroundOverlay(mapOverlay).setBearing(10);

        setUpGraphNodes(); // store coordinates of nodes
        setUpGraphEdges();
        setUpBuildingButtons();

        // LEAVE FOR TESTING:
        //drawAllNodes();
        //drawAllGraphEdges();
        //drawRoute(new int[]{}); // FOR TESTING ONLY
    }

    @SuppressLint("MissingPermission")
    protected void locationGranted() {
//        map.setOnMyLocationButtonClickListener(this);
//        map.setOnMyLocationClickListener(this);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

    protected void setUserLat(double lat) {
        userLat = lat;
    }

    protected void setUserLong(double lng) {
        userLong = lng;
    }

    protected boolean userLocationIsNull() {
        return userLat == null || userLong == null;
    }

    protected ArrayList<String> getWrittenDirections() {
        return currentDirections;
    }

    private void displayWrittenInstructions() {
        homeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instructionsTextView.setText(currentDirections.get(0));
                showInstructionViews();
                if(ttsEnabled){
                    HomeActivity.t1.speak(instructionsTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        instructionDisplayedNum = 0;
    }

    protected void calculateShortestRoute(int closestNodeNum, int destinationNodeNum) {
        BackendAPI backendAPI = new BackendAPI(MainActivity.getContext());
        backendapi.fetchReports();

        // TODO: EXAMPLE REPORTED EDGES CHANGE
        AvoidTheseEdges reported = new AvoidTheseEdges();
//        reported.addEdge(1, 3);
//        reported.addEdge(2, 5);

        backendAPI.calculateRoute(closestNodeNum, destinationNodeNum, reported, new Callback() {
            // TODO: Handle errors better
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: Probably will crash as not wrapped in runOnUiThread
                Log.e("myapp", Log.getStackTraceString(e));
                Toast.makeText(MainActivity.getContext(), "An error has occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getBoolean("success")) {
                        // ACTUAL RESULTS HERE
                        Integer estimatedDistance = result.getInt("estimatedDistance");
                        Double estimatedTime = result.getDouble("estimatedTime");

                        setJourneyDetails(estimatedTime, estimatedDistance);

                        JSONArray _route = result.getJSONArray("route");
                        int[] route = new int[_route.length()];
                        for (int i = 0; i < _route.length(); i++){
                            route[i] = _route.getInt(i);
                        }

                        // For debugging:
//                        Log.d("test", String.valueOf(closestNodeNum));
//                        Log.d("test", String.valueOf(destinationNodeNum));
//                        Log.d("test", String.valueOf(_route));
//                        Log.d("test", Arrays.toString(route));

                        // DRAW ROUTE (MUST BE IN MAIN THREAD FOR GOOGLE MAPS API)
                        homeActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawRoute(route);
                            }
                        });

                        JSONArray _directions = result.getJSONArray("directions");
                        currentDirections = new ArrayList<>();
                        for (int i = 0; i < _directions.length(); i++){
                            currentDirections.add(_directions.getString(i));
                        }

                        displayWrittenInstructions();

                        Log.v("myapp", estimatedDistance.toString());
                        Log.v("myapp", estimatedTime.toString());
                        Log.v("myapp", Arrays.toString(route));
                        Log.v("myapp", currentDirections.toString());
                    }
                } catch (Exception e) {
                    // TODO: Handle errors better
                    Log.e("myapp", Log.getStackTraceString(e));
                }
            }
        });
    }

    protected Node findClosestNodeToUser(LatLng userLocation) {
        double distance;
        double shortestDistance = Double.POSITIVE_INFINITY;
        Node closestNode = null;

        for (Node node : nodes) {
            if (node != null) {
                distance = SphericalUtil.computeDistanceBetween(userLocation, node.coordinatesLatLng);

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestNode = node;
                }
                //Log.d("distance", String.valueOf(distance));
            }
        }
        return closestNode;
    }

    protected boolean onDestinationSelected(String destinationName) {
        if (userLocationIsNull()) {
            Toast.makeText(HomeActivity.getAppContext(), "Waiting for location data. Please wait and try again.", Toast.LENGTH_LONG).show();
            return false;
        }
        // Clear (any) current map objects:
        clearMapObjects();

        int destinationNodeNumber = Arrays.asList(nodeNames).indexOf(destinationName) + 1;
        LatLng currentUserLocation = new LatLng(userLat, userLong);
        Node closestNode = findClosestNodeToUser(currentUserLocation);

        calculateShortestRoute(closestNode.nodeNumber, destinationNodeNumber);

        return true;
    }

//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(MainActivity.getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
//                .show();
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        Toast.makeText(MainActivity.getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
//                .show();
//        // Return false so that we don't consume the event and the default behavior still occurs
//        // (the camera animates to the user's current position).
//        return false;
//    }

    // Check which line is clicked
    protected void polyLineClickCheck(boolean routeDisplayed){
        Button report = homeActivity.findViewById(R.id.button3);

        // If the route is not already displayed
        if(!routeDisplayed){
            drawAllNodes();
            drawAllGraphEdges();
        }
        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(@NonNull Polyline polyline) {
                for(Polyline line: polylineGraphEdgeHashMap.keySet()){
                    if(line.equals(polyline)){
                        Log.d("debug", line.getId());
                        line.setColor(R.color.white);
                        line.setZIndex(1);
                        report.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reportToBackend(polylineGraphEdgeHashMap.get(line));
                            }
                        });
                    }else{
                        line.setColor(0xffff0000);
                        line.setZIndex(0);
                    }
                }
            }
        });
    }

    public void displayReportedEdgeOnMap(Polyline line, boolean routeDisplayed, String start, String end){
        if(!routeDisplayed){
            drawAllGraphEdges();
            drawAllNodes();
        }
        line.setColor(R.color.teal_200);
        Toast.makeText(homeActivity.getApplicationContext(), "Reported path between : " + start + " " + end, Toast.LENGTH_LONG).show();
    }

    private void reportToBackend(GraphEdge edge){
        Node nodeA = edge.getNodeA();
        Node nodeB = edge.getNodeB();

        String comment = "An incident has occurred on this edge with unknown consequences.";
        backendapi.submitReport(nodeA.nodeNumber, nodeB.nodeNumber, comment, new Callback() {
            // TODO: Handle errors better
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: Probably will crash as not wrapped in runOnUiThread
                Log.e("myapp", Log.getStackTraceString(e));
                Toast.makeText(backendapi.context, "An error has occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    if (result.getBoolean("success")) {
                        ((Activity)backendapi.context).runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(backendapi.context, "Report submitted successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    } else {
                        ((Activity)backendapi.context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(backendapi.context, result.getString("error"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    // TODO: Handle errors better
                                    Log.e("myapp", Log.getStackTraceString(e));
                                }
                            }
                        });
                        backendapi.fetchReports();
                    }
                } catch (Exception e) {
                    // TODO: Handle errors better
                    Log.e("myapp", Log.getStackTraceString(e));
                }
            }
        });
    }
}