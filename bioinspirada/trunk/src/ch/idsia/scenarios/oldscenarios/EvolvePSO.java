/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.scenarios.oldscenarios;

import ch.idsia.agents.Agent;
import ch.idsia.agents.learning.MLPPSOAgent;
import ch.idsia.agents.learning.MediumMLPPSOAgent;
import ch.idsia.agents.learning.MediumMLPAgent;
import ch.idsia.agents.learning.SimpleMLPAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.ea.ES;
import ch.idsia.evolution.ea.PSO;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.wox.serial.Easy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 4, 2009
 * Time: 4:33:25 PM
 */

public class EvolvePSO
{


public static void main(String[] args)
{
    int generations = 100;
    int populationSize = 150;



    int[] generationList = new int[]{20, 40, 60, 80, 100};
    int[] populationList = new int[]{50, 100, 300};
    int[] styles = new int[]{PSO.TOTALMENTE_CONECTADO, PSO.ANEL};

    MarioAIOptions options = new MarioAIOptions(args);
    List<Agent> bestAgents = new ArrayList<Agent>();
    DecimalFormat df = new DecimalFormat("0000");


    for (int x1=0; x1<styles.length; x1++) {
        for (int x2=0; x2<populationList.length;x2++) {
            int neighborhoodStyle = styles[x1];
            boolean mostrarVisualizacao = false;
            populationSize = populationList[x2];
            // String xmlName = "evolved-vizinhanca:"+neighborhoodStyle+"-"+GlobalOptions.getTimeStamp()+".xml";



            for (int difficulty = 0; difficulty < 11; difficulty++)
            {
                String xmlName = "evolved-vizinhanca:"+neighborhoodStyle+"-diff:"+difficulty+"-"+GlobalOptions.getTimeStamp()+".xml";
                System.out.println("New Evolve phase with difficulty = " + difficulty + " started.");

                // AGENTE A SER UTILIZADO


                MediumMLPPSOAgent initial =  new MediumMLPPSOAgent(populationSize, neighborhoodStyle);
                options.setLevelDifficulty(difficulty);
                options.setAgent((Agent) initial);

                options.setVisualization(false);
                options.setScale2X(true);
                Task task = new ProgressTask(options);

                PSO es = new PSO(task, initial, populationSize, neighborhoodStyle);

                // initial.reset();

                double bestScore = 0;
                String fileName;

                for (int gen = 0; gen < generations; gen++)
                {
                    es.nextGeneration();
                    double bestResult = es.getBestFitnesses()[0];

                    //System.out.println("Generation " + gen + " best " + bestResult);
                    if (bestResult > bestScore || gen == generations-1)
                    {
                        bestScore = bestResult;
                        Easy.save(es.getBests()[0], xmlName);
                        options.setVisualization(mostrarVisualizacao);
                    }

                    MediumMLPPSOAgent mlpAgent = new MediumMLPPSOAgent(es.getBestMLP());
                    Agent a = (Agent) mlpAgent;
                    a.setName(((Agent) initial).getName() + df.format(gen));
                    bestAgents.add(a);
                    double result = task.evaluate(a);
                    options.setVisualization(false);

                    if ( gen > 0 && gen % 20 == 0) {

                        System.out.println("--Generation "+gen+" Population Size "+populationSize+" Difficulty " + difficulty + " Neighborhood "+neighborhoodStyle+"0 best " + bestScore);
                    }

                    if (result > 4000)

                        break; // Go to next difficulty.


                }
                System.out.println("Generations "+generations+" Population Size "+populationSize+" Difficulty " + difficulty + " Neighborhood "+neighborhoodStyle+"0 best " + bestScore);
            }
        }
    }


    System.exit(0);
}
}
