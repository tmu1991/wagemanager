package com.wz.wagemanager.tools;//package com.wz.wagemanager.tools;
//
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.HibernateException;
//import org.hibernate.MappingException;
//import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
//import org.hibernate.engine.spi.SessionImplementor;
//import org.hibernate.id.Configurable;
//import org.hibernate.id.IdentifierGenerator;
//import org.hibernate.internal.SessionImpl;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.type.Type;
//
//import java.io.Serializable;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Properties;
//
///**
// * @author WindowsTen
// * @date 2018/5/4 11:34
// * @description
// */
//@Slf4j
//public class ShortUUIDIncrementGenerator implements IdentifierGenerator, Configurable {
//
//    private final String sql = "select uuid_short()";
//
//    @Override
//    public void configure (Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
//
//    }
//
//    @Override
//    public Serializable generate (SessionImplementor sessionImplementor, Object object) throws HibernateException {
//        synchronized (this) {
//            try {
//                PreparedStatement st =  sessionImplementor.connection ().prepareStatement (sql);
//                try {
//                    ResultSet rs = st.executeQuery ();
//                    final long result;
//                    try {
//                        rs.next ();
//                        result = rs.getString (1);
//                    } finally {
//                        rs.close ();
//                    }
//                    log.debug ("GUID identifier generated: " + result);
//                    return result;
//                } finally {
//                    st.close ();
//                    sessionImplementor.connection ().close ();
//                }
//            } catch (SQLException e) {
//                throw new SqlExceptionHelper().convert (e,"could not retrieve GUID",sql);
//            }
//        }
//    }
//}
