package com.tests;

import com.Manager;
import org.junit.jupiter.api.BeforeEach;

class ManagerTest {

    Manager manager;

    @BeforeEach
    void setUp() {
        manager = new Manager();
    }

    //Добавление субтаска. (условия к примеру нового сабтаска)
    //Результат:
//    @org.junit.jupiter.api.Test
//    void test_addSubtask() {
//        Manager manager = new Manager();
//        Subtask st = new Subtask();
//        st.setName("HZ");
//        st.setDescription("HerZnaet");
//        st.setId(-1);
//        Epic epic = new Epic();
//        Subtask newSt = manager.addSubtask(epic, st);
//
//        Subtask stFromStorage = manager.getSubtaskByID(newSt.getId());
//
//        Assertions.assertNotNull(stFromStorage);
//        Assertions.assertEquals(newSt.getId(), stFromStorage.getId());
//    }


    // Изменяем статус на Done.
    // Исходные Данные: В эпике ВСЕ сабтаски имеют статус NEW.
    // Результат: Эпик переходит в статус IN_PROGRESS
//    @org.junit.jupiter.api.Test
//    void test_subtackCahngeStatusToDone() {
//        Manager manager = new Manager();
//
//        Epic epic = createTestEpic(2, TypeOfStatus.NEW);
//        Subtask st = epic.subtaskList.get(0);
//
//        Subtask changedSt = manager.getSubtaskByID(st.getId());
//        changedSt.setStatus(TypeOfStatus.DONE);
//        manager.changeSubtask(changedSt);
//
//        Epic epic = manager.getEpicById(changedSt.getEpic().getId());
//        Assertions.assertEquals(TypeOfStatus.IN_PROGRESS, epic.getStatus());
//
//    }
//
//    // Изменяем статус на Done.
//    // Исходные Данные: В эпике ВСЕ КРОМЕ ОДНОГО сабтаска имеют статус DONE.
//    // Результат: Эпик переходит в статус DONE
//    @org.junit.jupiter.api.Test
//    void test_subtackCahngeStatusToDone() {
//        Manager manager = new Manager();
//
//        Epic epic = createTestEpic(3, TypeOfStatus.DONE);
//        Subtask st = new Subtask();
//        st.setStatus(TypeOfStatus.NEW);
//        manager.addSubtask(epic, st);
//
//        Subtask changedSt = manager.getSubtaskByID(st.getId());
//        changedSt.setStatus(TypeOfStatus.DONE);
//        manager.changeSubtask(changedSt);
//
//        Epic epic = manager.getEpicById(changedSt.getEpic().getId());
//        Assertions.assertEquals(TypeOfStatus.DONE, epic.getStatus());
//
//    }
//
//    private Epic createTestEpic(int countSubtasks, TypeOfStatus subtaskStatus){
//        Epic epic = new Epic();
//        manager.addEpic(epic);
//
//        for (int i =0; i<countSubtasks; ++i){
//            Subtask st = new Subtask();
//            st.setStatus(subtaskStatus);
//            manager.addSubtask(epic, st);
//        }
//
//
//        return epic;
//    }
}
