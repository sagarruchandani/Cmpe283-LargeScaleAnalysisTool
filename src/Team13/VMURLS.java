package Team13;

import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

public class VMURLS
{
	// Common between Admin vCenter and Team vCenter
	public String username() { return "administrator" ; }
    public String password() { return "12!@qwQW" ; }
    
    // Team vCenter
    public URL URL_host() throws Exception {return new URL("https://130.65.133.10/sdk"); }
    public ServiceInstance si_team() throws Exception {return new ServiceInstance(URL_host(), username(), password(), true); }
    public Folder RootFolder_team() throws Exception { return si_team().getRootFolder();};
    public ManagedEntity[] mes_team() throws Exception{return new InventoryNavigator(RootFolder_team()).searchManagedEntities("HostSystem");}
    
    // Admin vCenter
    public URL URL_vcenter() throws Exception {return new URL("https://130.65.132.14/sdk") ; }
    public ServiceInstance si_admin() throws Exception {return new ServiceInstance(URL_vcenter(), username(), password(), true); }
    public Folder RootFolder_admin() throws Exception { return si_admin().getRootFolder();}
    public ManagedEntity[] mes_admin() throws Exception{return new InventoryNavigator(RootFolder_admin()).searchManagedEntities("HostSystem");}
    
}