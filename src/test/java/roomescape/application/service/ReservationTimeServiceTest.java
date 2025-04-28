package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    private static final ReservationTime TEST_RESERVATION_TIME = new ReservationTime(null, DEFAULT_TEST_TIME);

    private ReservationTimeService reservationTimeService;
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new FakeReservationTimeDao(TEST_RESERVATION_TIME);
        reservationTimeService = new ReservationTimeService(reservationTimeDao);
    }

    @Test
    void 전체_예약_시간_목록을_조회한다() {
        // when
        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(1)
        );
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
    void 예약_시간을_삭제한_경우_TRUE를_반환한다() {
        // when
        boolean result = reservationTimeService.deleteReservationTime(TEST_RESERVATION_ID);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 없는_예약_시간을_삭제한_경우_FALSE를_반환한다() {
        // when
        boolean result = reservationTimeService.deleteReservationTime(2L);

        // then
        assertThat(result).isFalse();
    }
}
