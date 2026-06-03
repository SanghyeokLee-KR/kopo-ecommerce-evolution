/* UserMapper - 관리자 회원 목록 조회 */
SELECT
    u.nb_user AS nbUser,
    u.id_user AS idUser,
    u.nm_user AS nmUser,
    u.no_mobile AS noMobile,
    u.nm_email AS nmEmail,
    u.st_status AS stStatus,
    u.cd_user_type AS cdUserType
FROM tb_user u
WHERE (
          :stStatus IS NULL
              OR u.st_status = :stStatus
          )
ORDER BY u.nb_user DESC
OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;


/* UserMapper - 관리자 회원 총 개수 */
SELECT COUNT(*)
FROM tb_user u
WHERE (
          :stStatus IS NULL
              OR u.st_status = :stStatus
          );