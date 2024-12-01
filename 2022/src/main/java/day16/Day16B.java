package day16;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day16B extends AbstractDay {

    private static final List<State> EMPTY_LIST = Collections.emptyList();

    public Day16B() {
        super("day16");
    }

    public static void main(String[] args) throws Exception {
        new Day16B().solution();
    }


    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        EnumMap<ValveRef, Valve> valves = getValves(lines);
        var states = new HashSet<State>();
        long t0 = System.currentTimeMillis();
        states.add(new State(ValveRef.AA, ValveRef.AA, 0, valves));
        range(0, 25).forEach(i -> {
            System.out.println("i:" + i);
            System.out.println("states.size:" + states.size());
            var newStates = new HashMap<Pair, List<State>>();
            states.stream().map(this::calculateNewStates)
                    .flatMap(Collection::stream)
                    .filter(stateToAdd -> newStates.getOrDefault(
                                    new Pair(stateToAdd.position1.name, stateToAdd.position2.name), EMPTY_LIST).stream()
                            .noneMatch(stateToAdd::isNotBetter))
                    .forEach(stateToAdd -> addNewStates(newStates, stateToAdd));
            states.clear();
            newStates.values().forEach(states::addAll);
            states.forEach(State::increaseFlow);
            int max = states.stream().mapToInt(State::getTotalFlow).max().orElseThrow();

            System.out.println("max: " + max);
            if (i > 16) {
                states.removeIf(s -> s.totalFlow < 5 * max / 6);
            } else if (i > 8) {
                states.removeIf(s -> s.totalFlow < 3 * max / 4);
            }
        });
        long t1 = System.currentTimeMillis();
        System.out.println("totalTime = " + (t1 - t0));
    }

    private void addNewStates(Map<Pair, List<State>> newStates, State stateToAdd) {
        var pair = new Pair(stateToAdd.position1.name, stateToAdd.position2.name);
        var statesGroup = newStates.getOrDefault(pair, new ArrayList<>());
        statesGroup.add(stateToAdd);
        statesGroup.removeIf(stateToAdd::isBetter);
        newStates.put(pair, statesGroup);
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
        if (!s.position1.isOpen() && !s.position2.isOpen()) {
            var state = s.copy();
            state.open1();
            state.open2();
            list.add(state);
        }
        if (!s.position1.isOpen()) {
            s.position2.valves.forEach(ref -> {
                var state = s.copy();
                state.open1();
                state.setPosition2(ref);
                list.add(state);
            });
        }
        if (!s.position2.isOpen()) {
            s.position1.valves.forEach(ref -> {
                var state = s.copy();
                state.open2();
                state.setPosition1(ref);
                list.add(state);
            });
        }
        s.position1.valves.forEach(ref1 ->
                s.position2.valves.forEach(ref2 -> {
                    var state = s.copy();
                    state.setPosition1(ref1);
                    state.setPosition2(ref2);
                    list.add(state);
                })
        );
        return list;
    }

    class State {
        Valve position1;
        Valve position2;
        int totalFlow;
        EnumMap<ValveRef, Valve> valves;
        EnumMap<ValveRef, Valve> notZeroValves;


        public State(ValveRef position1, ValveRef position2, int totalFlow, EnumMap<ValveRef, Valve> valves) {
            this.totalFlow = totalFlow;
            this.valves = valves;
            this.position1 = valves.get(position1);
            this.position2 = valves.get(position2);
            notZeroValves = new EnumMap<>(ValveRef.class);
            valves.values().stream().filter(Valve::isNotEmptyFlow)
                    .forEach(zValve -> notZeroValves.put(zValve.name, zValve));
        }

        public State copy() {
            EnumMap<ValveRef, Valve> valvesCopy = new EnumMap<>(ValveRef.class);
            valves.values().forEach(v -> valvesCopy.put(v.name, new Valve(v)));
            return new State(position1.name, position2.name, totalFlow, valvesCopy);
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
            State s = (State) o;
            return totalFlow == s.totalFlow &&
                    ((position1.name == s.position1.name && position2.name == s.position2.name)
                            || (position2.name == s.position1.name && position1.name == s.position2.name)) &&
                    notZeroValves.keySet().stream().allMatch(k -> valves.get(k).open == s.valves.get(k).open);
        }

        boolean isBetter(State s) {
            var b1 = notZeroValves.keySet().stream()
                    .allMatch(k -> valves.get(k).open || (!valves.get(k).open && !s.valves.get(k).open));
            var b2 = new Pair(s.position1.name, s.position2.name).equals(new Pair(position1.name, position2.name));
            return b2 && b1 && totalFlow > s.totalFlow;
        }

        boolean isNotBetter(State s) {
            return s.isBetter(this);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position1, position2, totalFlow, notZeroValves);
        }

        public void open1() {
            position1.open = true;
        }

        public void open2() {
            position2.open = true;
        }

        public void setPosition1(ValveRef ref) {
            position1 = valves.get(ref);
        }

        public void setPosition2(ValveRef ref) {
            position2 = valves.get(ref);
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

        public boolean isNotEmptyFlow() {
            return flowRate != 0;
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
            Valve v = (Valve) o;
            return open == v.open && name == v.name;
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

    class Pair {

        ValveRef r1;
        ValveRef r2;

        public Pair(ValveRef r1, ValveRef r2) {
            this.r1 = r1;
            this.r2 = r2;
        }

        @Override
        public boolean equals(Object o) {
            Pair p = (Pair) o;
            return (r1 == p.r1 && r2 == p.r2) || (r1 == p.r2 && r2 == p.r1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(r1, r2);
        }
    }

}

