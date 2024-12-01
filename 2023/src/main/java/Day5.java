import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public class Day5 extends AbstractDay {

    record Interval(long min, long max) {
        Interval before(Interval i) {
            if (i.min >= min) {
                return null;
            }
            return new Interval(i.min, min - 1);
        }

        Interval after(Interval i) {
            if (i.max <= max) {
                return null;
            }
            return new Interval(max + 1, i.max);
        }

        Interval inside(Interval i) {
            return new Interval(Math.max(min, i.min), Math.min(max, i.max));
        }
    }

    record Convertor(long destination, long source, long range) {
        Convertor(String[] inputs) {
            this(parseLong(inputs[0]), parseLong(inputs[1]), parseLong(inputs[2]));
        }

        boolean contains(long n) {
            return n >= source && n < source + range;
        }

        long convert(long n) {
            return n + destination - source;
        }

        boolean contains(Interval i) {
            return contains(i.min) || contains(i.max);
        }

        Interval convert(Interval i) {
            return new Interval(convert(i.min), convert(i.max));
        }

        Interval getInterval() {
            return new Interval(source, source + range - 1);
        }
    }

    record ConversionMap(List<Convertor> convertors, ConversionMap nextConvertor) {
        long convert(long n) {
            long conversion = convertors.stream().filter(c -> c.contains(n)).findAny().map(c -> c.convert(n)).orElse(n);
            return nextConvertor == null ? conversion : nextConvertor.convert(conversion);
        }

        List<Interval> convert(Interval interval) {
            List<Interval> intervals = new ArrayList<>();
            intervals.add(interval);
            List<Interval> convertedIntervals = new ArrayList<>();
            for (Convertor convertor : convertors) {
                var convertorInterval = convertor.getInterval();
                for (var intersection : intervals.stream().filter(convertor::contains).toList()) {
                    convertedIntervals.add(convertor.convert(convertorInterval.inside(intersection)));
                    Optional.ofNullable(convertorInterval.before(intersection)).ifPresent(intervals::add);
                    Optional.ofNullable(convertorInterval.after(intersection)).ifPresent(intervals::add);
                    intervals.remove(intersection);
                }
            }
            convertedIntervals.addAll(intervals);
            return nextConvertor == null ? convertedIntervals :
                    convertedIntervals.stream().map(nextConvertor::convert).flatMap(Collection::stream).toList();
        }
    }

    public Day5() {
        super("day5");
    }


    public static void main(String[] args) throws Exception {
        new Day5().solution();
    }

    @Override
    public void solution() throws Exception {
        try (var lines = Files.lines(getPath())) {
            var inputs = lines.toList();
            var line0 = inputs.get(0);
            var seeds = stream(line0.substring(line0.indexOf(':') + 2).split(" ")).map(Long::parseLong).toList();

            int seedToSoilIndex = inputs.indexOf("seed-to-soil map:");
            int soilToFertilizerIndex = inputs.indexOf("soil-to-fertilizer map:");
            int fertilizerToWaterIndex = inputs.indexOf("fertilizer-to-water map:");
            int waterToLightIndex = inputs.indexOf("water-to-light map:");
            int lightToTemperatureIndex = inputs.indexOf("light-to-temperature map:");
            int temperatureToHumidityIndex = inputs.indexOf("temperature-to-humidity map:");
            int humidityToLocationIndex = inputs.indexOf("humidity-to-location map:");
            var humidityToLocation = new ConversionMap(range(humidityToLocationIndex + 1, inputs.size())
                    .mapToObj(i -> new Convertor(inputs.get(i).split(" "))).toList(), null);
            var temperatureToHumidity = map(temperatureToHumidityIndex, humidityToLocationIndex, humidityToLocation, inputs);
            var lightToTemperature = map(lightToTemperatureIndex, temperatureToHumidityIndex, temperatureToHumidity, inputs);
            var waterToLight = map(waterToLightIndex, lightToTemperatureIndex, lightToTemperature, inputs);
            var fertilizerToWater = map(fertilizerToWaterIndex, waterToLightIndex, waterToLight, inputs);
            var soilToFertilizer = map(soilToFertilizerIndex, fertilizerToWaterIndex, fertilizerToWater, inputs);
            var seedToSoil = map(seedToSoilIndex, soilToFertilizerIndex, soilToFertilizer, inputs);
            System.out.println(seeds.stream().mapToLong(seedToSoil::convert).min().orElse(0));

            var seeds2 = range(0, seeds.size() / 2).mapToObj(k ->
                    new Interval(seeds.get(k * 2), seeds.get(k * 2) + seeds.get(k * 2 + 1) - 1)).toList();
            var intervals = seeds2.stream().map(seedToSoil::convert).flatMap(Collection::stream).toList();
            System.out.println(intervals.size());
            System.out.println(intervals.stream().mapToLong(Interval::min).min().orElse(0));
        }
    }

    ConversionMap map(int start, int end, ConversionMap next, List<String> inputs) {
        var convertors = range(start + 1, end - 1).mapToObj(i -> new Convertor(inputs.get(i).split(" "))).toList();
        return new ConversionMap(convertors, next);
    }

}
