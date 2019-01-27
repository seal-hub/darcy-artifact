package md.utm.fcim.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import md.utm.fcim.dto.NodeDto;
import md.utm.fcim.service.NodeService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeServiceImpl implements NodeService {

    public List<NodeDto> findAll() {
        ObjectMapper mapper = new ObjectMapper();
        List<NodeDto> nodes = new ArrayList<>();

        try {
            // Convert JSON string from file to Object
            nodes = mapper.readValue(
                    new File("/home/veladii/IdeaProjects/Pad_Lab2/common/src/main/resources/nodes_config.json"),
                    new TypeReference<List<NodeDto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }
}
