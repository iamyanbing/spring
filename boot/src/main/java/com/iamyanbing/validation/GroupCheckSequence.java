package com.iamyanbing.validation;

import javax.validation.GroupSequence;

/**
 * 请求参数检验顺序定义。
 * @author : HuangYanBing
 * @date 2022/11/11 16:13
 */
@GroupSequence({GroupCheckSequence.GroupA.class, GroupCheckSequence.GroupB.class, GroupCheckSequence.GroupC.class,
        GroupCheckSequence.GroupD.class, GroupCheckSequence.GroupE.class, GroupCheckSequence.GroupF.class,
        GroupCheckSequence.GroupG.class, GroupCheckSequence.GroupH.class, GroupCheckSequence.GroupI.class,
        GroupCheckSequence.GroupJ.class, GroupCheckSequence.GroupK.class, GroupCheckSequence.GroupL.class,
        GroupCheckSequence.GroupM.class, GroupCheckSequence.GroupN.class, GroupCheckSequence.GroupO.class,
        GroupCheckSequence.GroupP.class, GroupCheckSequence.GroupQ.class, GroupCheckSequence.GroupR.class,})
public interface GroupCheckSequence {
    interface GroupA {
    }

    interface GroupB {
    }

    interface GroupC {
    }

    interface GroupD {
    }

    interface GroupE {
    }

    interface GroupF {
    }

    interface GroupG {
    }

    interface GroupH {
    }

    interface GroupI {
    }

    interface GroupJ {
    }

    interface GroupK {
    }

    interface GroupL {
    }

    interface GroupM {
    }

    interface GroupN {
    }

    interface GroupO {
    }

    interface GroupP {
    }

    interface GroupQ {
    }

    interface GroupR {
    }

}
