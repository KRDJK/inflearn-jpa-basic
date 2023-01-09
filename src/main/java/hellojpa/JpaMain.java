package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // em을 하나 만들면 db 커넥션을 하나 얻었다고 생각하면 된다.
        EntityManager em = emf.createEntityManager(); // EntityManager 안에는 영속성 컨텍스트가 1:1로 들어있다.

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // code
        try {
//            Member findMember = em.find(Member.class, 2L); // 이렇게 가져오는 순간 JPA가 이 객체를 관리한다.
            
            // 비영속 상태
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            // 영속 상태
            System.out.println("=== BEFORE ===");
            em.persist(member);
//            em.detach(member); // 회원 entity를 영속성 컨텍스트에서 분리시킴. (준영속 상태)
//            em.remove(member); // DB에서 데이터 삭제
            System.out.println("=== AFTER ===");

            Member findMember = em.find(Member.class, 101L); // 오잉 왜 select 쿼리에 저장이 안됐나..? 위에서 1차 캐시에 저장되었기 때문
                                // 스냅샷을 뜬다.
            System.out.println("findMember = " + findMember);

            tx.commit(); // 커밋하는 시점에 관리하던 객체의 변동사항을 체크 (변경감지) 후 변동이 있다면 update 문을 날린다.
                    // 엔티티와 스냅샷을 비교 (변경 감지)
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // 성공이거나 실패거나 얘는 닫아줘야 함. db 커넥션 자체는 끊어줘야지!
        }
        emf.close();
    }
}
