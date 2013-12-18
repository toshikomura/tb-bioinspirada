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

package ch.idsia.evolution.ea;

import ch.idsia.agents.Agent;
import ch.idsia.agents.learning.MLPPSOAgent;
import ch.idsia.agents.learning.MediumMLPAgent;
import ch.idsia.agents.learning.MediumMLPPSOAgent;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.evolution.EA;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.MLP;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Apr 29, 2009
 * Time: 12:16:49 PM
 */
public class PSO implements EA
{

private final MediumMLPPSOAgent PSO;
private final float[] fitness;
private final Task task;
private final int evaluationRepetitions = 1;
    private final int populationSize;

    public static final int TOTALMENTE_CONECTADO = 0;
    public static final int ANEL = 1;

public PSO(Task task, MLPPSOAgent initial, int populationSize, int neighborhood)
{
    //this.PSO = new MediumMLPPSOAgent(populationSize, neighborhood);
    this.PSO = (MediumMLPPSOAgent)initial;
    this.fitness = new float[populationSize];
    this.task = task;
    this.populationSize = populationSize;
}

public void nextGeneration()
{
    PSO.mutate();
    for (int i = 0; i < populationSize; i++)
    {
        evaluate(i);
    }
    PSO.setBests(fitness);
}

private void evaluate(int which)
{
    fitness[which] = 0;
    for (int i = 0; i < evaluationRepetitions; i++)
    {
        PSO.particles[which][1].reset();

        // AGENTE A SER UTILIZADO
        MediumMLPPSOAgent mlpAgent = new MediumMLPPSOAgent(populationSize, 0);

        mlpAgent.setgBest(PSO.particles[which][1]);
        fitness[which] += task.evaluate((Agent) mlpAgent);
//            System.out.println("which " + which + " fitness " + fitness[which]);
    }
    fitness[which] = fitness[which] / evaluationRepetitions;
}


public Evolvable[] getBests()
{
    return new Evolvable[]{PSO.gBest};
}

    public MLP getBestMLP() {
        return PSO.gBest;
    }


public float[] getBestFitnesses()
{
    return new float[]{PSO.gBestValue};
}

}
