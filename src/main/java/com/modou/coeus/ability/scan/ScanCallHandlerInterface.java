package com.modou.coeus.ability.scan;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;

public interface ScanCallHandlerInterface {

    void invoke(ScanCallHandlerData scanCallHandlerData);
}
