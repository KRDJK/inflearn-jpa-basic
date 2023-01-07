package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // em을 하나 만들면 db 커넥션을 하나 얻었다고 생각하면 된다.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // code
        try {
            Member findMember = em.find(Member.class, 2L); // 이렇게 가져오는 순간 JPA가 이 객체를 관리한다.
            findMember.setName("HelloJPA");

            tx.commit(); // 커밋하는 시점에 관리하던 객체의 변동사항을 체크 (변경감지) 후 변동이 있다면 update 문을 날린다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // 성공이거나 실패거나 얘는 닫아줘야 함. db 커넥션 자체는 끊어줘야지!
        }
        emf.close();
    }
}
