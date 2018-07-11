package cl.valpoSystems.bpmTraking;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.drools.core.process.instance.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import cl.valpoSystems.bpmTraking.orm.client.BpmInstanciaHistoricaMapper;
import cl.valpoSystems.bpmTraking.orm.model.BpmInstanciaHistorica;

/**
 * Work Item Handler para Traking!
 *
 */
public class WihTraking implements WorkItemHandler  {
    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
    }
    
    public SqlSession getSession() {
		SqlSessionFactory sqlSessionFactory = null;
		SqlSession sqlSession = null;
		try {
			String resource = "cl/valpoSystems/bpmTraking/SCompras.conexion.xml";
			Reader reader = Resources.getResourceAsReader (resource);
			
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sqlSessionFactory.openSession();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sqlSession;
	}

	public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
		// TODO Auto-generated method stub		
	}

	public void executeWorkItem(WorkItem wi, WorkItemManager wiM) {
		// TODO Auto-generated method stub
		 
		String sisUsuario = (String) wi.getParameter("sisUsuario");
		String idSolicitud = (String) wi.getParameter("idSolicitud");
		String idOc = (String) wi.getParameter("idOc");
		String idStatus= (String) wi.getParameter("idStatus");
		String idProceso = (String) wi.getParameter("idProceso");
		
		this.guardaTraking(Long.toString(wi.getProcessInstanceId()), wi.getName(), idProceso,  idSolicitud,  idOc,  Integer.parseInt(idStatus),  sisUsuario);
		wiM.completeWorkItem(wi.getId(), null);		
	}
	
	public void guardaTraking(String IdInstancia, String IdActividad, String IdProceso,  String IdSolicitud,  String IdOc,  Integer IdStatus,  String IdUsuario) {
		BpmInstanciaHistorica b = new BpmInstanciaHistorica();
		b.setIdInstancia(IdInstancia);
		b.setIdActividad(IdActividad);
		b.setIdProceso(IdProceso);
		b.setIdSolicitud(IdSolicitud);
		b.setIdOc(IdOc);
		b.setIdStatus(IdStatus);
		b.setIdUsuario(IdUsuario);
		
		SqlSession sqlSession = this.getSession();
		sqlSession.getMapper(BpmInstanciaHistoricaMapper.class).insertSelective(b);
		sqlSession.commit();
		
	}
	
}
