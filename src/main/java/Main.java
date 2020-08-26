import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.Factory;
import io.jenetics.SinglePointCrossover;
import io.jenetics.GaussianMutator;
import io.jenetics.Mutator;


public class Main
{
    static int maxGen = 1000;

    private static int fitnessFunction(Genotype<BitGene> gt)
    {
        return gt.chromosome().
                as(BitChromosome.class).
                bitCount();
    }

    public static void main (String[] args)
    {
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<BitGene>> gtf =
                Genotype.of(BitChromosome.of(15, 0.5));

        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(Main::fitnessFunction, gtf)
                .populationSize(400)
                .selector(new TournamentSelector<>(2))
                .alterers(
                        new Mutator<>(0.02),
                        new SinglePointCrossover<>(0.6))
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        Phenotype<BitGene, Integer> best = engine.stream()
                .limit(maxGen)
                .peek(statistics)
                .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best.genotype().chromosome().toString() + " " + best.generation() + " " + best.age(maxGen));
    }
}
