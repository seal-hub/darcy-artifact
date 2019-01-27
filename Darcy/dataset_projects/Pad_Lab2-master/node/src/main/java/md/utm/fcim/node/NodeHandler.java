package md.utm.fcim.node;

import md.utm.fcim.service.NodeService;
import md.utm.fcim.service.impl.NodeServiceImpl;

public class NodeHandler {

    private NodeService nodeService;

    NodeHandler() {
        this.nodeService = new NodeServiceImpl();
    }

    public void createNodes() {
        this.nodeService.findAll()
                .forEach(nodeDescription -> {
                    new Thread(() -> {
                        new Node(nodeDescription);
                    }).start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
