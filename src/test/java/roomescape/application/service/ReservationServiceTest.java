package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;

class ReservationServiceTest {

    private static final String TEST_NAME = "testName";
    private static final ReservationTime TEST_RESERVATION_TIME = new ReservationTime(null, LocalTime.MIDNIGHT);
    private static final Reservation TEST_RESERVATION = new Reservation(null, TEST_NAME, LocalDate.MAX,
            TEST_RESERVATION_TIME);

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new FakeReservationTimeDao(TEST_RESERVATION_TIME);
        reservationDao = new FakeReservationDao(TEST_RESERVATION);
        reservationService = new ReservationService(reservationDao, reservationTimeDao);
    }

    @Test
    void 전체_예약_목록을_조회한다() {
        // when
        List<ReservationResponse> result = reservationService.getReservations();

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.getFirst().name()).isEqualTo(TEST_NAME)
        );
    }

    @Test
    void 예약을_추가한다() {
        // given
        ReservationRequest request = new ReservationRequest("test", LocalDate.MIN, 1L);

        // when
        ReservationResponse result = reservationService.saveReservation(request);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.name()).isEqualTo(request.name()),
                () -> assertThat(result.date()).isEqualTo(request.date())
        );
    }

    @Test
    void 예약을_삭제한_경우_TRUE를_반환한다() {
        // when
        boolean result = reservationService.deleteReservation(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 없는_예약을_삭제한_경우_FALSE를_반환한다() {
        // when
        boolean result = reservationService.deleteReservation(2L);

        // then
        assertThat(result).isFalse();
    }
}
