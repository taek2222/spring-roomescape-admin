package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationTimeDao;

class ReservationTimeServiceTest {

    private static final LocalTime DEFAULT_TEST_TIME = LocalTime.MIDNIGHT;
    private static final Long TEST_RESERVATION_ID = 1L;
    private static final List<ReservationTime> INITIAL_RESERVATION_TIMES = List.of(
            new ReservationTime(null, DEFAULT_TEST_TIME)
    );

    private ReservationTimeService reservationTimeService;
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new FakeReservationTimeDao(INITIAL_RESERVATION_TIMES);
        reservationTimeService = new ReservationTimeService(reservationTimeDao);
    }

    @Test
    void 전체_예약_시간_목록을_조회한다() {
        // when
        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();

        // then
        assertThat(result.size()).isEqualTo(INITIAL_RESERVATION_TIMES.size());
    }

    @Test
    void 예약_시간을_추가한다() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.MIDNIGHT);

        // when
        ReservationTimeResponse result = reservationTimeService.saveReservationTime(request);

        // then
        assertThat(result.startAt())
                .isEqualTo(request.startAt().toString());
    }

    @Test
    void 예약_시간을_삭제한다() {
        // when
        boolean isDeleted = reservationTimeService.deleteReservationTime(TEST_RESERVATION_ID);

        // then
        assertThat(isDeleted).isTrue();
    }
}
