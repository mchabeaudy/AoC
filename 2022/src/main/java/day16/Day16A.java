package day16;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day16A extends AbstractDay {

    private static final List<State> EMPTY_LIST = Collections.emptyList();

    public Day16A() {
        super("day16");
    }

    public static void main(String[] args) throws Exception {
        new Day16A().solution();
    }


    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        EnumMap<ValveRef, Valve> valves = getValves(lines);
        var states = new HashSet<State>();
        states.add(new State(ValveRef.AA, 0, valves));
        range(0, 29).forEach(i -> {
            var newStates = new EnumMap<ValveRef, List<State>>(ValveRef.class);
            states.stream().map(this::calculateNewStates)
                    .flatMap(Collection::stream)
                    .filter(stateToAdd -> newStates.getOrDefault(stateToAdd.current.name, EMPTY_LIST).stream()
                            .noneMatch(stateToAdd::isNotBetter))
                    .forEach(stateToAdd -> addNewStates(newStates, stateToAdd));
            states.clear();
            newStates.values().forEach(states::addAll);
            states.forEach(State::increaseFlow);
        });
        int max = states.stream().mapToInt(State::getTotalFlow).max().orElseThrow();
        System.out.println(max);
    }

    private void addNewStates(EnumMap<ValveRef, List<State>> newStates, State stateToAdd) {
        var statesGroup = newStates.getOrDefault(stateToAdd.current.name, new ArrayList<>());
        statesGroup.add(stateToAdd);
        statesGroup.removeIf(stateToAdd::isBetter);
        newStates.put(stateToAdd.current.name, statesGroup);
    }

    private EnumMap<ValveRef, Valve> getValves(List<String> lines) {
        var valvesMap = lines.stream()
                .map(line -> new Valve(ValveRef.fromName(line.substring(6, 8)),
                        line.substring(line.indexOf('=') + 1, line.indexOf(';')),
                        line.contains(",") ?
                                Arrays.stream(line.substring(line.indexOf(',') - 2).split(", "))
                                        .map(ValveRef::fromName).toList()
                                : List.of(ValveRef.fromName(line.substring(line.lastIndexOf(' ') + 1)))))
                .collect(Collectors.toMap(v -> v.name, v -> v));
        var valves = new EnumMap<ValveRef, Valve>(ValveRef.class);
        valves.putAll(valvesMap);
        return valves;
    }

    List<State> calculateNewStates(State s) {
        if (s.canWait()) {
            return List.of(s);
        }
        var list = new ArrayList<State>();
        if (!s.current.isOpen()) {
            var copyValves = new EnumMap<ValveRef, Valve>(ValveRef.class);
            s.valves.values().forEach(v -> copyValves.put(v.name, new Valve(v)));
            copyValves.get(s.current.name).open = true;
            list.add(new State(s.current.name, s.totalFlow, copyValves));
        }
        s.current.valves.stream().map(v -> {
                    var copyValves = new EnumMap<ValveRef, Valve>(ValveRef.class);
                    s.valves.values().forEach(val -> copyValves.put(val.name, new Valve(val)));
                    return new State(v, s.totalFlow, copyValves);
                })
                .forEach(list::add);
        return list;
    }

    class State {

        Valve current;
        int totalFlow;
        EnumMap<ValveRef, Valve> valves;
        EnumMap<ValveRef, Valve> notZeroValves;


        public State(ValveRef valve, int totalFlow, EnumMap<ValveRef, Valve> valves) {
            this.totalFlow = totalFlow;
            this.valves = valves;
            current = valves.get(valve);
            notZeroValves = new EnumMap<>(ValveRef.class);
            valves.values().stream().filter(v -> v.flowRate != 0)
                    .forEach(zValve -> notZeroValves.put(zValve.name, zValve));
        }

        public int getTotalFlow() {
            return totalFlow;
        }

        void increaseFlow() {
            totalFlow += notZeroValves.values().stream().filter(Valve::isOpen).mapToInt(Valve::getFlowRate).sum();
        }


        public boolean canWait() {
            return notZeroValves.values().stream().allMatch(Valve::isOpen);
        }

        @Override
        public boolean equals(Object o) {
            State state = (State) o;
            return totalFlow == state.totalFlow && current.name == state.current.name && notZeroValves.keySet().stream()
                    .allMatch(k -> valves.get(k).open == state.valves.get(k).open);
        }

        boolean isBetter(State s) {
            var b = notZeroValves.keySet().stream()
                    .allMatch(k -> valves.get(k).open || (!valves.get(k).open && !s.valves.get(k).open));
            return b && totalFlow > s.totalFlow;
        }

        boolean isNotBetter(State s) {
            return s.isBetter(this);
        }

        @Override
        public int hashCode() {
            return Objects.hash(current, totalFlow, notZeroValves);
        }

    }


    class Valve {

        boolean open;
        int flowRate;
        List<ValveRef> valves;
        ValveRef name;

        public Valve(Valve v) {
            this.open = v.open;
            this.flowRate = v.flowRate;
            this.valves = v.valves;
            this.name = v.name;
        }

        public Valve(ValveRef name, String flowRate, List<ValveRef> valves) {
            this.name = name;
            this.flowRate = Integer.parseInt(flowRate);
            this.valves = valves;
            open = false;
        }

        public boolean isOpen() {
            return open;
        }

        @Override
        public String toString() {
            return name.name();
        }

        public int getFlowRate() {
            return flowRate;
        }

        @Override
        public boolean equals(Object o) {
            Valve valve = (Valve) o;
            return open == valve.open && name == valve.name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(open, name);
        }
    }

    enum ValveRef {
        AA, BB, CC, DD, EE, FF, GG, HH, TU, LQ, TY, PZ, YL, QQ, IK, IM, UZ, IN, AH, QZ, IZ, ZL, VH, VI, VJ, RG, AZ, JC,
        NJ, VS, RO, JH, FE, FH, BF, RY, FM, RZ, NW, JZ, OB, OF, WN, BZ, OH, OI, GC, GH, SY, SZ, GO, OY, XF, XG, PA,
        CU, TH, TM, TR, TS, II, JJ;

        static ValveRef fromName(String name) {
            return Arrays.stream(values()).filter(v -> v.name().equals(name))
                    .findAny().orElseThrow();
        }
    }

}

