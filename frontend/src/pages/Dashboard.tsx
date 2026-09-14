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

    useEffect(() => {
        async function loadDashboardData() {
            try {
                setLoading(true);
                const requests = await getRequests();
                const pending = requests.filter((r) => r.state === 'PENDING');
                setPendingCount(pending.length);

                const docs = await getDocuments();

                const entries = docs.filter((d) => d.operationType === 'ENTRY').slice(0, 5);
                const withdrawals = docs.filter((d) => d.operationType === 'WITHDRAWAL').slice(0, 5);

                setRecentEntries(entries);
                setRecentWithdrawals(withdrawals);

                const warehouses = await getWarehouses();
                const stockLists = await Promise.all(warehouses.map((w) => getWarehouseStock(w.id)));
                const allStock = stockLists.flat();
                const low = allStock.filter((l) => l.currentStock <= l.minimumStock);
                setLowStockCount(low.length);

            } catch (error) {
                console.error("Failed to fetch Dashboard data.", error);
            } finally {
                setLoading(false);
            }
        }

        loadDashboardData();
    }, []);

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
                            {t.lowStockAlerts}
                        </span>
                        <p className="text-3xl font-bold text-gray-900 mt-2">
                            {loading ? '...' : lowStockCount}{' '}
                            <span className="text-base font-normal text-gray-400">{t.itemsCountUnit}</span>
                        </p>
                        <span className="text-xs text-gray-500 mt-1 block">
                            {t.belowMinStockDesc}
                        </span>
                    </div>
                    <div className="p-4 bg-red-50 text-red-600 rounded-2xl">
                        <AlertTriangle className="w-8 h-8" />
                    </div>
                </div>

                
                <div className="bg-white rounded-xl border border-amber-200 p-6 shadow-xs flex items-center justify-between">
                    <div>
                        <span className="text-sm font-bold text-amber-600 uppercase tracking-wider block">
                            {t.pendingRequests}
                        </span>
                        <p className="text-3xl font-bold text-gray-900 mt-2">
                            {loading ? '...' : pendingCount}{' '}
                            <span className="text-base font-normal text-gray-400">{t.requestsCountUnit}</span>
                        </p>
                        <span className="text-xs text-gray-500 mt-1 block">
                            {t.awaitingPreparationDesc}
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
                                <h3 className="font-bold text-gray-900 text-base">{t.recentEntries}</h3>
                                <p className="text-xs text-gray-400">{t.recentEntriesDesc}</p>
                            </div>
                        </div>
                        <span className="text-xs font-bold text-emerald-700 bg-emerald-50 px-2.5 py-1 rounded-md border border-emerald-200">
                            {t.entry.toUpperCase()}
                        </span>
                    </div>

                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-50 text-gray-500 font-semibold border-b border-gray-200 text-xs uppercase tracking-wider">
                            <tr>
                                <th className="px-6 py-3">{t.docNumber}</th>
                                <th className="px-6 py-3">{t.supplier}</th>
                                <th className="px-6 py-3">{t.date}</th>
                                <th className="px-6 py-3 text-right">{t.quantityShort}</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100 text-gray-700">
                            {loading ? (
                                <tr>
                                    <td colSpan={4} className="px-6 py-4 text-center text-gray-400">{t.loading}</td>
                                </tr>
                            ) : recentEntries.length === 0 ? (
                                <tr>
                                    <td colSpan={4} className="px-6 py-4 text-center text-gray-400">{t.noEntriesRecorded}</td>
                                </tr>
                            ) : (
                                recentEntries.map((doc) => {
                                    const totalQty = doc.items?.reduce((acc, item) => acc + item.quantity, 0) ?? 0;
                                    return (
                                        <tr key={doc.id} className="hover:bg-gray-50/80 transition-colors">
                                            <td className="px-6 py-3.5 font-bold text-gray-900">
                                                {doc.internalDocumentNumber || doc.originDocumentNumber || `#${doc.id}`}
                                            </td>
                                            <td className="px-6 py-3.5">{doc.externalEntity?.name ?? '—'}</td>
                                            <td className="px-6 py-3.5 text-gray-500">{doc.documentDate}</td>
                                            <td className="px-6 py-3.5 text-right font-semibold text-emerald-600">
                                                +{totalQty}
                                            </td>
                                        </tr>
                                    );
                                })
                            )}
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
                                <h3 className="font-bold text-gray-900 text-base">{t.recentWithdrawals}</h3>
                                <p className="text-xs text-gray-400">{t.recentWithdrawalsDesc}</p>
                            </div>
                        </div>
                        <span className="text-xs font-bold text-rose-700 bg-rose-50 px-2.5 py-1 rounded-md border border-rose-200">
                            {t.withdrawal.toUpperCase()}
                        </span>
                    </div>

                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-50 text-gray-500 font-semibold border-b border-gray-200 text-xs uppercase tracking-wider">
                            <tr>
                                <th className="px-6 py-3">{t.docNumber}</th>
                                <th className="px-6 py-3">{t.destinationArea}</th>
                                <th className="px-6 py-3">{t.date}</th>
                                <th className="px-6 py-3 text-right">{t.quantityShort}</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100 text-gray-700">
                            {loading ? (
                                <tr>
                                    <td colSpan={4} className="px-6 py-4 text-center text-gray-400">{t.loading}</td>
                                </tr>
                            ) : recentWithdrawals.length === 0 ? (
                                <tr>
                                    <td colSpan={4} className="px-6 py-4 text-center text-gray-400">{t.noWithdrawalsRecorded}</td>
                                </tr>
                            ) : (
                                recentWithdrawals.map((doc) => {
                                    const totalQty = doc.items?.reduce((acc, item) => acc + item.quantity, 0) ?? 0;
                                    return (
                                        <tr key={doc.id} className="hover:bg-gray-50/80 transition-colors">
                                            <td className="px-6 py-3.5 font-bold text-gray-900">
                                                {doc.internalDocumentNumber || doc.originDocumentNumber || `#${doc.id}`}
                                            </td>
                                            <td className="px-6 py-3.5">{doc.destinationServiceArea?.name ?? '—'}</td>
                                            <td className="px-6 py-3.5 text-gray-500">{doc.documentDate}</td>
                                            <td className="px-6 py-3.5 text-right font-semibold text-rose-600">
                                                -{totalQty}
                                            </td>
                                        </tr>
                                    );
                                })
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

