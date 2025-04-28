package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final AtomicLong sequence = new AtomicLong();
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    public FakeReservationTimeDao(List<ReservationTime> reservationTimes) {
        reservationTimes.forEach(this::save);
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public ReservationTime findById(Long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime toSave = createReservationTime(reservationTime);
        validateDuplicateId(toSave);
        reservationTimes.add(toSave);
        return toSave;
    }

    @Override
    public boolean deleteById(Long id) {
        return reservationTimes.removeIf(existingTime -> existingTime.getId().equals(id));
    }

    private ReservationTime createReservationTime(ReservationTime reservationTime) {
        if (reservationTime.getId() == null) {
            long newId = sequence.incrementAndGet();
            return new ReservationTime(newId, reservationTime);
        }
        return reservationTime;
    }

    private void validateDuplicateId(ReservationTime toSave) {
        boolean isDuplicated = reservationTimes.stream()
                .anyMatch(existingTime -> existingTime.getId().equals(toSave.getId()));

        if (isDuplicated) {
            throw new IllegalArgumentException("중복된 ID가 존재합니다.");
        }
    }
}
