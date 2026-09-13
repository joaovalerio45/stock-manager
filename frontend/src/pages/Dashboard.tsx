import { AlertTriangle, Clock, ArrowDownLeft, ArrowUpRight } from "lucide-react";
import { useLanguage } from "../context/LanguageContext";
import { useEffect, useState } from "react";
import type { Document } from "../types/document";
import { getRequests } from "../services/requestService";
import { getDocuments } from "../services/documentService";
import { getWarehouses, getWarehouseStock } from "../services/warehouseService";

export function Dashboard() {
    const { t } = useLanguage();

    const [pendingCount, setPendingCount] = useState<number>(0);
    const [lowStockCount, setLowStockCount] = useState<number>(0);
    const [recentEntries, setRecentEntries] = useState<Document[]>([]);
    const [recentWithdrawals, setRecentWithdrawals] = useState<Document[]>([]);

    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() =>{
        (async function loadDashboardData() {
            try{
                setLoading(true);
                const requests = await getRequests();
                const pending = requests.filter((r) => r.state === 'PENDING' );
                setPendingCount(pending.length);

                const docs = await getDocuments();

                const entries = docs.filter((d) => d.operationType === 'ENTRY').slice(0,5);
                const withdrawals = docs.filter((d) => d.operationType === 'WITHDRAWAL').slice(0,5);

                setRecentEntries(entries);
                setRecentWithdrawals(withdrawals);

                const warehouses = await getWarehouses();
                const stockLists = await Promise.all(warehouses.map((w) => getWarehouseStock(w.id)))
                const allStock = stockLists.flat();
                const low = allStock.filter((l) => l.currentStock <= l.minimumStock);
                setLowStockCount(low.length);

            }catch(error){
                console.error("Failed to fetch Dashboard data.", error);
            }finally{
                setLoading(false);
            }
        }, [])
        loadDashboardData();
    })

    return (
        <div className="max-w-7xl mx-auto px-6 py-8 space-y-8">
            
            <div>
                <h1 className="text-3xl font-bold text-gray-900">
                    {t.dashboard}
                </h1>
            </div>

            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                
                <div className="bg-white rounded-xl border border-red-200 p-6 shadow-xs flex items-center justify-between">
                    <div>
                        <span className="text-sm font-bold text-red-600 uppercase tracking-wider block">
                            Alertas de Stock Baixo
                        </span>
                        <p className="text-3xl font-bold text-gray-900 mt-2">
                            3 <span className="text-base font-normal text-gray-400">artigos</span>
                        </p>
                        <span className="text-xs text-gray-500 mt-1 block">
                            Abaixo do stock mínimo configurado
                        </span>
                    </div>
                    <div className="p-4 bg-red-50 text-red-600 rounded-2xl">
                        <AlertTriangle className="w-8 h-8" />
                    </div>
                </div>

                
                <div className="bg-white rounded-xl border border-amber-200 p-6 shadow-xs flex items-center justify-between">
                    <div>
                        <span className="text-sm font-bold text-amber-600 uppercase tracking-wider block">
                            Pedidos Pendentes
                        </span>
                        <p className="text-3xl font-bold text-gray-900 mt-2">
                            5 <span className="text-base font-normal text-gray-400">pedidos</span>
                        </p>
                        <span className="text-xs text-gray-500 mt-1 block">
                            A aguardar preparação no armazém
                        </span>
                    </div>
                    <div className="p-4 bg-amber-50 text-amber-600 rounded-2xl">
                        <Clock className="w-8 h-8" />
                    </div>
                </div>
            </div>

            
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                
                <div className="bg-white rounded-xl border border-gray-200 shadow-xs overflow-hidden">
                    <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between bg-gray-50/50">
                        <div className="flex items-center gap-2.5">
                            <div className="p-2 bg-emerald-50 text-emerald-600 rounded-lg">
                                <ArrowDownLeft className="w-5 h-5" />
                            </div>
                            <div>
                                <h3 className="font-bold text-gray-900 text-base">Últimas Entradas</h3>
                                <p className="text-xs text-gray-400">Últimos 5 documentos de fornecedores</p>
                            </div>
                        </div>
                        <span className="text-xs font-bold text-emerald-700 bg-emerald-50 px-2.5 py-1 rounded-md border border-emerald-200">
                            ENTRADAS
                        </span>
                    </div>

                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-50 text-gray-500 font-semibold border-b border-gray-200 text-xs uppercase tracking-wider">
                            <tr>
                                <th className="px-6 py-3">Nº Documento</th>
                                <th className="px-6 py-3">Fornecedor</th>
                                <th className="px-6 py-3">Data</th>
                                <th className="px-6 py-3 text-right">Qtd</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100 text-gray-700">
                            <tr className="hover:bg-gray-50/80 transition-colors">
                                <td className="px-6 py-3.5 font-bold text-gray-900">ENT-2026/001</td>
                                <td className="px-6 py-3.5">Makro Portugal</td>
                                <td className="px-6 py-3.5 text-gray-500">11/09/2026</td>
                                <td className="px-6 py-3.5 text-right font-semibold text-emerald-600">+120</td>
                            </tr>
                            <tr className="hover:bg-gray-50/80 transition-colors">
                                <td className="px-6 py-3.5 font-bold text-gray-900">ENT-2026/002</td>
                                <td className="px-6 py-3.5">Staples Office</td>
                                <td className="px-6 py-3.5 text-gray-500">10/09/2026</td>
                                <td className="px-6 py-3.5 text-right font-semibold text-emerald-600">+45</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                
                <div className="bg-white rounded-xl border border-gray-200 shadow-xs overflow-hidden">
                    <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between bg-gray-50/50">
                        <div className="flex items-center gap-2.5">
                            <div className="p-2 bg-rose-50 text-rose-600 rounded-lg">
                                <ArrowUpRight className="w-5 h-5" />
                            </div>
                            <div>
                                <h3 className="font-bold text-gray-900 text-base">Últimas Saídas</h3>
                                <p className="text-xs text-gray-400">Últimos 5 consumos de departamentos</p>
                            </div>
                        </div>
                        <span className="text-xs font-bold text-rose-700 bg-rose-50 px-2.5 py-1 rounded-md border border-rose-200">
                            SAÍDAS
                        </span>
                    </div>

                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-50 text-gray-500 font-semibold border-b border-gray-200 text-xs uppercase tracking-wider">
                            <tr>
                                <th className="px-6 py-3">Nº Documento</th>
                                <th className="px-6 py-3">Destino (Área)</th>
                                <th className="px-6 py-3">Data</th>
                                <th className="px-6 py-3 text-right">Qtd</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100 text-gray-700">
                            <tr className="hover:bg-gray-50/80 transition-colors">
                                <td className="px-6 py-3.5 font-bold text-gray-900">SAI-2026/015</td>
                                <td className="px-6 py-3.5">Cozinha Principal</td>
                                <td className="px-6 py-3.5 text-gray-500">11/09/2026</td>
                                <td className="px-6 py-3.5 text-right font-semibold text-rose-600">-30</td>
                            </tr>
                            <tr className="hover:bg-gray-50/80 transition-colors">
                                <td className="px-6 py-3.5 font-bold text-gray-900">SAI-2026/016</td>
                                <td className="px-6 py-3.5">Lavandaria</td>
                                <td className="px-6 py-3.5 text-gray-500">10/09/2026</td>
                                <td className="px-6 py-3.5 text-right font-semibold text-rose-600">-12</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

