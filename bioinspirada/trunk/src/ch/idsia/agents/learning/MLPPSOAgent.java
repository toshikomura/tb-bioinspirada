/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
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

package ch.idsia.agents.learning;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.MLP;
import ch.idsia.evolution.ea.PSO;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 13, 2009
 * Time: 11:11:33 AM
 */
public class MLPPSOAgent extends BasicMarioAIAgent implements Agent, Evolvable
{

protected int numberOfOutputs;
    protected int numberOfInputs;
    protected int numberOfHidden;

    protected int populationSize;
public MLP[][] particles;
    protected MLP[] pBestList;
    public MLP gBest;

    protected float[] pBestValues;
    public float gBestValue;

    private int neighborhood;

public MLPPSOAgent(String name, int numberOfInputs, int numberOfHidden, int numberOfOutputs, int populationSize, int neighborhood)
{
    super(name);
    this.numberOfOutputs = numberOfOutputs;
    this.numberOfHidden = numberOfHidden;
    this.numberOfInputs = numberOfInputs;
    this.populationSize = populationSize;
    this.neighborhood = neighborhood;
    initializePSO(populationSize);
}

    public  MLPPSOAgent(String name, MLP mlp)
    {
        super(name);
        this.gBest = mlp;
        this.numberOfInputs = mlp.getNumberOfInputs();
        this.numberOfOutputs = mlp.getNumberOfOutputs();
        this.numberOfHidden = mlp.getNumberOfHidden();
    }
      /*
private MLPPSOAgent(String name, MLP mlp)
{
    super(name);
}       */

    private void initializePSO(int populationSize)
    {
        this.particles = new MLP[populationSize][2];
        this.pBestList = new MLP[populationSize];
        this.pBestValues = new float[populationSize];
        this.populationSize = populationSize;

        for (int i = 0; i < populationSize; i++) {
            particles[i][0] = new MLP(this.numberOfInputs, numberOfHidden, numberOfOutputs);
            particles[i][0].randomise();
            particles[i][1] = particles[i][0].copy();

            pBestList[i] = particles[i][0].copy();
            pBestValues[i] = 0;
        }
        gBest = particles[0][0];
        gBestValue = 0;
    }

public Evolvable getNewInstance()
{
    MLPPSOAgent newAgent = new MLPPSOAgent(this.name, this.numberOfInputs, this.numberOfHidden, numberOfOutputs, this.populationSize, this.neighborhood);

    for (int i=0; i<populationSize; i++) {
        newAgent.particles[i][0] = this.particles[i][0].copy();
        newAgent.particles[i][1] = this.particles[i][1].copy();
        newAgent.pBestValues[i] = this.pBestValues[i];
    }
    newAgent.gBest = this.gBest.copy();
    newAgent.gBestValue = gBestValue;

    return newAgent;
}

public Evolvable copy()
{
    MLPPSOAgent newAgent = new MLPPSOAgent(this.name, this.numberOfInputs, this.numberOfHidden, this.numberOfOutputs, this.populationSize, this.neighborhood);

    for (int i=0; i<populationSize; i++) {
        newAgent.particles[i][0] = this.particles[i][0].copy();
        newAgent.particles[i][1] = this.particles[i][1].copy();
        newAgent.pBestValues[i] = this.pBestValues[i];
    }
    newAgent.gBest = this.gBest.copy();
    newAgent.gBestValue = gBestValue;

    return newAgent;
}

public void reset()
{
    for (int i=0; i<populationSize; i++) {
        pBestValues[i] = 0;
    }
    gBest.reset();
    gBestValue = 0;
}

public void mutate()
{
    for (int i = 0; i < populationSize; i++) {
        MLP temp = particles[i][1].copy();
        //agent.setMlp(particulas[i][1]);
        //particulas[i][1].getInputs();
        particles[i][1].psoRecombine(particles[i][0], pBestList[i], getGBest(i, neighborhood));
        particles[i][0] = temp;
    }
    //mlp.mutate();

}

    public void setBests(float[] fitness)
    {
        for (int i=0; i<populationSize; i++)
        {
            if (fitness[i] > pBestValues[i]) {
                pBestValues[i] = fitness[i];
                pBestList[i] = particles[i][1].copy();
            }

            if (fitness[i] > gBestValue) {
                gBestValue = fitness[i];
                gBest = particles[i][1].copy();
            }
        }
    }

    public MLP getGBest(int i, int neighborhood)
    {
        MLP mlp = gBest;
        switch (neighborhood)
        {
            case PSO.TOTALMENTE_CONECTADO:
                // gBest = gBest
                break;
            case PSO.ANEL:
                mlp = pBestList[i];
                float bestMlp = pBestValues[i];

                int n = next(i, 1, populationSize);
                if (pBestValues[n] > bestMlp)
                {
                    bestMlp = pBestValues[n];
                    mlp = pBestList[n];
                }

                n = next(i, -1, populationSize);
                if (pBestValues[n] > bestMlp)
                {
                    bestMlp = pBestValues[n];
                    mlp = pBestList[n];
                }

                break;
        }
        return mlp;
    }

    private int next(int base, int dir, int count)
    {
        int n = base + dir;
        if (n < 0) return count+n;
        if (n >= count) return n-count;
        return n;
    }

public boolean[] getAction()
{

//        byte[][] enemies = observation.getEnemiesObservation(/*0*/);
    double[] inputs = getInputs();
    double[] outputs = gBest.propagate(inputs);
    boolean[] action = new boolean[numberOfOutputs];
    for (int i = 0; i < action.length; i++)
    {
        action[i] = outputs[i] > 0;
    }
    return action;
}

    public void setgBest(MLP gBest)
    {
        this.gBest = gBest;
    }


    public String getName()
{
    return name;
}
    public double[] getInputs() {
        double[] inputs = new double[numberOfInputs];
        return inputs;
    }
public void setName(String name)
{
}
}
