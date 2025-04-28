package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

public class FakeReservationDao implements ReservationDao {

    private final AtomicLong sequence = new AtomicLong();
    private final List<Reservation> reservations = new ArrayList<>();

    public FakeReservationDao(List<Reservation> reservations) {
        reservations.forEach(this::save);
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation toSave = createReservation(reservation);
        validateDuplicateId(toSave);
        reservations.add(toSave);
        return toSave;
    }

    @Override
    public boolean deleteById(Long id) {
        return reservations.removeIf(existing -> existing.getId().equals(id));
    }

    private Reservation createReservation(Reservation reservation) {
        if (reservation.getId() == null) {
            long newId = sequence.incrementAndGet();
            return new Reservation(newId, reservation);
        }
        return reservation;
    }

    private void validateDuplicateId(Reservation toSave) {
        boolean isDuplicated = reservations.stream()
                .anyMatch(existing -> existing.getId().equals(toSave.getId()));

        if (isDuplicated) {
            throw new IllegalArgumentException("중복된 ID가 존재합니다.");
        }
    }
}
