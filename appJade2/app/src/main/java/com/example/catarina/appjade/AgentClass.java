package com.example.catarina.appjade;

import jade.core.Agent;
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
            DFService.deregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ReceiverMessage extends CyclicBehaviour {


        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                ACLMessage reply = msg.createReply();

                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    if (msg.getContent().equals("shutdown")) {
                        myAgent.doDelete();
                    }
                    if (msg.getContent().equals("info")) {
                        System.out.println("aaa");
                        ActivityAgent f = new ActivityAgent();
                        Info info = f.getInformacao();
                        reply.setContent(info.toString());
                        reply.setPerformative(ACLMessage.INFORM);
                        myAgent.send(reply);
                    }

                }
            }
            block();
        }
    }

}