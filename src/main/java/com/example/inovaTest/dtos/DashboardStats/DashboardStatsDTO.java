
package com.example.inovaTest.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDTO {
    // Corresponde à interface DashboardStats no frontend
    private long notificacoesNaoLidas;
    private long reservasProximas;
    private long ocorrenciasAbertas;
}