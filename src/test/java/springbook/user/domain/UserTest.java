package springbook.user.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test()
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), equalTo(level.nextLevel()));
        }
    }

    @Test
    public void cannotUpgradeLevel() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Level[] levels = Level.values();
            for (Level level : levels) {
                if (level.nextLevel() != null) {
                    continue;
                }
                user.setLevel(level);
                user.upgradeLevel();
            }
        });
    }

}