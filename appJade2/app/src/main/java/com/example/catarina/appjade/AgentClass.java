package com.example.catarina.appjade;

import android.content.Context;
import android.util.Log;

import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * Created by Catarina on 23/11/2015.
 */
public class AgentClass extends Agent {

    private static final long serialVersionUID = 1594371294421614291L;
    protected void setup() {
        super.setup();

        DFAgentDescription dfa = new DFAgentDescription();
        dfa.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("AndroidAgent");
        dfa.addServices(sd);

        try {
            Gcontext.a=this;
            DFService.register(this, dfa);

        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new ReceiverMessage());


    }

    @Override
    protected void takeDown() {
        // TODO Auto-generated method stub
        super.takeDown();
        try {


            DFService.deregister(Gcontext.a);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void DesligarAgent(){
        Gcontext.a.doDelete();
    }
    private class ReceiverMessage extends CyclicBehaviour {


        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();

            if (msg != null) {

                Context c=Gcontext.c;
                Sensor s=new Sensor(c);
                Info info=s.getAll();
                ACLMessage reply = msg.createReply();

                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    if (msg.getContent().equals("shutdown")) {
                        myAgent.doDelete();
                    } else if (msg.getContent().equals("0")) {
                        //System.out.println("0");
                        reply.setContent("mblw/+/" + info.getBateria() + "/+/" + info.getLocalizacao() + "/+/" + info.getWifi());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    } else if (msg.getContent().equals("1")) {
                        //System.out.println("1");
                        reply.setContent("mb/+/" + info.getBateria());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);

                    } else if (msg.getContent().equals("2")) {
                        //System.out.println("2");
                        reply.setContent("ml/+/" + info.getLocalizacao());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    } else if (msg.getContent().equals("3")) {
                        //System.out.println("3");
                        reply.setContent("mw/+/" + info.getWifi());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    } else if (msg.getContent().equals("4")) {
                        //System.out.println("4");
                        reply.setContent("mbl/+/" + info.getBateria() + "/+/" + info.getLocalizacao());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    } else if (msg.getContent().equals("5")) {
                        //System.out.println("5");
                        reply.setContent("mbw/+/" + info.getBateria() + "/+/" + info.getWifi());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    } else if (msg.getContent().equals("6")) {
                        //System.out.println("6");
                        reply.setContent("mlw/+/" + info.getLocalizacao() + "/+/" + info.getWifi());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    }

                }
            }
            block();
        }
    }
}