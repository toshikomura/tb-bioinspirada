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

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 13, 2009
 * Time: 11:11:33 AM
 */
public class MediumMLPPSOAgent extends MLPPSOAgent
{

private static final String name = "MediumMLPPSOAgent";

                       /*
final int numberOfOutputs = Environment.numberOfKeys;
//    final int numberOfInputs = 53;
final int numberOfInputs = 28;
    final int numberOfHidden = 10;
                         */

public MediumMLPPSOAgent(int populationSize, int neighborhood)
{
    //era 21 ou 28 ou 101
    super(name, 53, 10, Environment.numberOfKeys, populationSize, neighborhood);
}
    public MediumMLPPSOAgent(MLP mlp)
    {
        super(name, 53, 10, Environment.numberOfKeys, 1, 0);
        gBest = mlp;

    }

    public double[] getInputs() {
        byte[][] scene = this.mergedObservation;
        double[] inputs = new double[numberOfInputs];
        int which = 0;
        for (int i = -2; i < 3; i++)
        {
            for (int j = -2; j < 3; j++)
            {
                inputs[which++] = probe(i, j, scene);
            }
        }
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                inputs[which++] = probe(i, j, enemies);
            }
        }
        inputs[inputs.length - 3] = isMarioOnGround ? 1 : 0;
        inputs[inputs.length - 2] = isMarioAbleToJump ? 1 : 0;
        inputs[inputs.length - 1] = 1;
           /*
        double[] inputs;// = new double[numberOfInputs];
        inputs = new double[numberOfInputs];
        int which = 0;
        for (int i = -3; i < 4; i++)
        {
            for (int j = -3; j < 4; j++)
            {
                inputs[which++] = probe(i, j, scene);
            }
        }
        for (int i = -3; i < 4; i++)
        {
            for (int j = -3; j < 4; j++)
            {
                inputs[which++] = probe(i, j, enemies);
            }
        }
        inputs[inputs.length - 3] = isMarioOnGround ? 1 : 0;
        inputs[inputs.length - 2] = isMarioAbleToJump ? 1 : 0;
        inputs[inputs.length - 1] = 1;
                     */

        /*
        double[] inputs = new double[]{probe(-1, -1, scene), probe(0, -1, scene), probe(1, -1, scene),
                probe(-1, 0, scene), probe(0, 0, scene), probe(1, 0, scene),
                probe(-1, 1, scene), probe(0, 1, scene), probe(1, 1, scene),
                //probe(-1, -1, enemies), probe(0, -1, enemies), probe(1, -1, enemies),
                //probe(-1, 0, enemies), probe(0, 0, enemies), probe(1, 0, enemies),
                //probe(-1, 1, enemies), probe(0, 1, enemies), probe(1, 1, enemies),
                isMarioOnGround ? 1 : 0, isMarioAbleToJump ? 1 : 0,
                1};    */
        return inputs;
    }

    private double probe(int x, int y, byte[][] scene)
{
    int realX = x + 11;
    int realY = y + 11;
    return (scene[realX][realY] != 0) ? 1 : 0;
}

}
