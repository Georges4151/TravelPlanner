package com.travelplanner.model.amap;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.List;

public class AMapDirectionResponse {
    private static final Gson gson = new Gson();
    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private String info;

    @SerializedName("infocode")
    private String infocode;

    @SerializedName("count")
    private String count;

    @SerializedName("route")
    private Route route;

    public boolean isSuccess() {
        return "1".equals(status);
    }

    public String getInfo() {
        return info;
    }

    public String getInfocode() {
        return infocode;
    }

    public Route getRoute() {
        return route;
    }

    public static class Route {
        @SerializedName("paths")
        private List<Path> paths;

        @SerializedName("transits")
        private List<Transit> transits;

        public List<Path> getPaths() {
            return paths;
        }

        public List<Transit> getTransits() {
            return transits;
        }
    }

    public static class Path {
        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("strategy")
        private String strategy;

        @SerializedName("tolls")
        private JsonElement tollsElement;

        @SerializedName("toll_distance")
        private JsonElement tollDistanceElement;

        @SerializedName("steps")
        private List<Step> steps;

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public int getTolls() {
            return getJsonIntValue(tollsElement);
        }

        public List<Step> getSteps() {
            return steps;
        }
    }

    public static class Step {
        @SerializedName("instruction")
        private JsonElement instructionElement;

        @SerializedName("road")
        private JsonElement roadElement;

        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("action")
        private JsonElement actionElement;

        @SerializedName("assistant_action")
        private JsonElement assistantActionElement;

        @SerializedName("polyline")
        private JsonElement polylineElement;

        public String getInstruction() {
            return getJsonStringValue(instructionElement);
        }

        public String getRoad() {
            return getJsonStringValue(roadElement);
        }

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public String getAction() {
            return getJsonStringValue(actionElement);
        }

        public String getAssistantAction() {
            return getJsonStringValue(assistantActionElement);
        }

        public String getPolyline() {
            return getJsonStringValue(polylineElement);
        }
    }

    public static class Transit {
        @SerializedName("cost")
        private JsonElement costElement;

        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("walking_distance")
        private JsonElement walkingDistanceElement;

        @SerializedName("transit_distance")
        private JsonElement transitDistanceElement;

        @SerializedName("transit_num")
        private JsonElement transitNumElement;

        @SerializedName("segments")
        private List<Segment> segments;

        public int getCost() {
            return getJsonIntValue(costElement);
        }

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public int getWalkingDistance() {
            return getJsonIntValue(walkingDistanceElement);
        }

        public int getTransitDistance() {
            return getJsonIntValue(transitDistanceElement);
        }

        public int getTransitNum() {
            return getJsonIntValue(transitNumElement);
        }

        public List<Segment> getSegments() {
            return segments;
        }
    }

    public static class Segment {
        @SerializedName("instruction")
        private String instruction;

        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("walking")
        private List<Walking> walking;

        @SerializedName("bus")
        private List<Bus> bus;

        @SerializedName("entrance")
        private JsonElement entranceElement;

        @SerializedName("exit")
        private JsonElement exitElement;

        @SerializedName("railway")
        private List<Railway> railway;

        public String getInstruction() {
            return instruction;
        }

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public Walking getWalking() {
            if (walking != null && !walking.isEmpty()) {
                return walking.get(0);
            }
            return null;
        }

        public Bus getBus() {
            if (bus != null && !bus.isEmpty()) {
                return bus.get(0);
            }
            return null;
        }

        public Entrance getEntrance() {
            if (entranceElement == null) {
                return null;
            }
            if (entranceElement.isJsonObject()) {
                return gson.fromJson(entranceElement, Entrance.class);
            } else if (entranceElement.isJsonArray() && entranceElement.getAsJsonArray().size() > 0) {
                return gson.fromJson(entranceElement.getAsJsonArray().get(0), Entrance.class);
            }
            return null;
        }

        public Exit getExit() {
            if (exitElement == null) {
                return null;
            }
            if (exitElement.isJsonObject()) {
                return gson.fromJson(exitElement, Exit.class);
            } else if (exitElement.isJsonArray() && exitElement.getAsJsonArray().size() > 0) {
                return gson.fromJson(exitElement.getAsJsonArray().get(0), Exit.class);
            }
            return null;
        }

        public Railway getRailway() {
            if (railway != null && !railway.isEmpty()) {
                return railway.get(0);
            }
            return null;
        }
    }

    public static class Walking {
        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("steps")
        private List<WalkingStep> steps;

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public List<WalkingStep> getSteps() {
            return steps;
        }
    }

    public static class WalkingStep {
        @SerializedName("instruction")
        private JsonElement instructionElement;

        @SerializedName("road")
        private JsonElement roadElement;

        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("action")
        private JsonElement actionElement;

        @SerializedName("assistant_action")
        private JsonElement assistantActionElement;

        @SerializedName("polyline")
        private JsonElement polylineElement;

        public String getInstruction() {
            return getJsonStringValue(instructionElement);
        }

        public String getRoad() {
            return getJsonStringValue(roadElement);
        }

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public String getAction() {
            return getJsonStringValue(actionElement);
        }

        public String getAssistantAction() {
            return getJsonStringValue(assistantActionElement);
        }

        public String getPolyline() {
            return getJsonStringValue(polylineElement);
        }
    }

    public static class Bus {
        @SerializedName("buslines")
        private List<BusLine> buslines;

        public List<BusLine> getBuslines() {
            return buslines;
        }
    }

    public static class BusLine {
        @SerializedName("name")
        private String name;

        @SerializedName("type")
        private String type;

        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        @SerializedName("departure_stop")
        private Stop departureStop;

        @SerializedName("arrival_stop")
        private Stop arrivalStop;

        @SerializedName("via_num")
        private JsonElement viaNumElement;

        @SerializedName("via_stops")
        private List<Stop> viaStops;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }

        public Stop getDepartureStop() {
            return departureStop;
        }

        public Stop getArrivalStop() {
            return arrivalStop;
        }

        public int getViaNum() {
            return getJsonIntValue(viaNumElement);
        }

        public List<Stop> getViaStops() {
            return viaStops;
        }
    }

    public static class Stop {
        @SerializedName("name")
        private String name;

        @SerializedName("location")
        private String location;

        @SerializedName("id")
        private String id;

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getId() {
            return id;
        }
    }

    public static class Entrance {
        @SerializedName("name")
        private String name;

        @SerializedName("location")
        private String location;

        @SerializedName("id")
        private String id;

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getId() {
            return id;
        }
    }

    public static class Exit {
        @SerializedName("name")
        private String name;

        @SerializedName("location")
        private String location;

        @SerializedName("id")
        private String id;

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getId() {
            return id;
        }
    }

    public static class Railway {
        @SerializedName("name")
        private String name;

        @SerializedName("type")
        private String type;

        @SerializedName("departure_stop")
        private Stop departureStop;

        @SerializedName("arrival_stop")
        private Stop arrivalStop;

        @SerializedName("departure_time")
        private String departureTime;

        @SerializedName("arrival_time")
        private String arrivalTime;

        @SerializedName("distance")
        private JsonElement distanceElement;

        @SerializedName("duration")
        private JsonElement durationElement;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Stop getDepartureStop() {
            return departureStop;
        }

        public Stop getArrivalStop() {
            return arrivalStop;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public int getDistance() {
            return getJsonIntValue(distanceElement);
        }

        public int getDuration() {
            return getJsonIntValue(durationElement);
        }
    }

    private static int getJsonIntValue(JsonElement element) {
        if (element == null) {
            return 0;
        }
        if (element.isJsonPrimitive()) {
            try {
                return Integer.parseInt(element.getAsString());
            } catch (NumberFormatException e) {
                return 0;
            }
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            if (array.size() > 0) {
                try {
                    return Integer.parseInt(array.get(0).getAsString());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    private static String getJsonStringValue(JsonElement element) {
        if (element == null) {
            return null;
        }
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            if (array.size() > 0) {
                return array.get(0).getAsString();
            }
        }
        return null;
    }
}
