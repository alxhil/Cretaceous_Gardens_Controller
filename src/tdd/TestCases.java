package tdd;

        import org.junit.Assert;
        import org.junit.Test;
        import security.Fence;
        import security.SecuritySystem;

public class TestCases {
    @Test
    public void test_SecuritySystem()
    {
        SecuritySystem securitySystem = new SecuritySystem();
        Fence fence = new Fence(74);
        Assert.assertEquals(true, securitySystem.isEmergencyOn(fence.isVoltageLow()));
    }



}
