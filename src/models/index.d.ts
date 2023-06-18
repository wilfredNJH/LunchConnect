import { ModelInit, MutableModel, __modelMeta__, ManagedIdentifier } from "@aws-amplify/datastore";
// @ts-ignore
import { LazyLoading, LazyLoadingDisabled } from "@aws-amplify/datastore";





type EagerNoteData = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<NoteData, 'id'>;
    readOnlyFields: 'createdAt' | 'updatedAt';
  };
  readonly id: string;
  readonly name: string;
  readonly description?: string | null;
  readonly image?: string | null;
  readonly createdAt?: string | null;
  readonly updatedAt?: string | null;
}

type LazyNoteData = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<NoteData, 'id'>;
    readOnlyFields: 'createdAt' | 'updatedAt';
  };
  readonly id: string;
  readonly name: string;
  readonly description?: string | null;
  readonly image?: string | null;
  readonly createdAt?: string | null;
  readonly updatedAt?: string | null;
}

export declare type NoteData = LazyLoading extends LazyLoadingDisabled ? EagerNoteData : LazyNoteData

export declare const NoteData: (new (init: ModelInit<NoteData>) => NoteData) & {
  copyOf(source: NoteData, mutator: (draft: MutableModel<NoteData>) => MutableModel<NoteData> | void): NoteData;
}