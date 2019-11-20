package tdd;

import org.junit.Assert;
import org.junit.Test;
import security.Fence;
import security.SecuritySystem;

public class TestCases {

    @Test
    public void test_SecuritySystem()
    {
        SecuritySystem securitySystem = new SecuritySystem(null);
        Fence fence = new Fence(securitySystem, 1);
        securitySystem.pushFence(fence);
        Assert.assertTrue(securitySystem.getFences().get(0).getFenceId() == fence.getFenceId());
    }

}
